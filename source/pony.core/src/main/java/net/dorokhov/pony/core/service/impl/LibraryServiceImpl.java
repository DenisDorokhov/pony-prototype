package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.domain.*;
import net.dorokhov.pony.core.service.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LibraryServiceImpl implements LibraryService {

	private static final int CLEANING_BUFFER_SIZE = 300;
	private static final String FILE_TAG_ARTWORK = "artwork";

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final Object lock = new Object();

	private TransactionTemplate transactionTemplate;

	private SongFileService songFileService;

	private ArtistService artistService;

	private AlbumService albumService;

	private SongService songService;

	private StoredFileService storedFileService;

	private SongDataReader songDataReader;

	@Autowired
	public void setTransactionManager(PlatformTransactionManager aTransactionManager) {
		transactionTemplate = new TransactionTemplate(aTransactionManager);
	}

	@Autowired
	public void setSongFileService(SongFileService aSongFileService) {
		songFileService = aSongFileService;
	}

	@Autowired
	public void setArtistService(ArtistService aArtistService) {
		artistService = aArtistService;
	}

	@Autowired
	public void setAlbumService(AlbumService aAlbumService) {
		albumService = aAlbumService;
	}

	@Autowired
	public void setSongService(SongService aSongService) {
		songService = aSongService;
	}

	@Autowired
	public void setStoredFileService(StoredFileService aStoredFileService) {
		storedFileService = aStoredFileService;
	}

	@Autowired
	public void setSongDataReader(SongDataReader aSongDataReader) {
		songDataReader = aSongDataReader;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public SongFile importSong(File aFile) {

		SongFile songFile;

		try {
			songFile = songFileService.getByPath(aFile.getAbsolutePath());
		} catch (Exception e) {

			log.error("could not get song file {}", aFile.getAbsolutePath(), e);

			throw new RuntimeException(e);
		}

		if (songFile == null || songFile.getUpdateDate().getTime() < aFile.lastModified()) {

			final SongData metaData;

			try {
				metaData = songDataReader.readSongData(aFile);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			synchronized (lock) {

				songFile = transactionTemplate.execute(new TransactionCallback<SongFile>() {

					@Override
					public SongFile doInTransaction(TransactionStatus status) {

						EntityModification<SongFile> songFile;

						try {
							songFile = importSongFile(metaData);
						} catch (Exception e) {

							log.error("could not import song file: {}", metaData.getPath(), e);

							throw new RuntimeException(e);
						}

						if (songFile.getEntity() != null &&
								songFile.getEntity().getName() != null &&
								songFile.getEntity().getArtist() != null &&
								songFile.getEntity().getAlbum() != null) {

							try {

								EntityModification<Artist> artist = importArtist(songFile.getEntity());
								EntityModification<Album> album = importAlbum(songFile.getEntity(), artist.getEntity());
								EntityModification<Song> song = importSong(songFile.getEntity(), album.getEntity());

								if (songFile.isModified() || song.isModified() || album.isModified() || artist.isModified()) {
									log.debug("song imported: {}", song.getEntity());
								}

							} catch (Exception e) {

								log.error("could not create song entities for song file: {}", songFile.getEntity(), e);

								throw new RuntimeException(e);
							}

						} else {
							log.warn("could not create song entities for inconsistent song file: {}", songFile.getEntity());
						}

						return songFile.getEntity();
					}
				});
			}
		}

		return songFile;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void normalizeSongs(List<File> aTargetFiles, ProgressHandler aHandler) {

		List<Integer> itemsToDelete = new ArrayList<Integer>();

		long processedItems = 0;

		Page<SongFile> page = songFileService.getAll(new PageRequest(0, CLEANING_BUFFER_SIZE, Sort.Direction.ASC, "id"));

		do {

			for (SongFile songFile : page.getContent()) {

				File file = new File(songFile.getPath());

				boolean shouldDelete = !file.exists();

				if (!shouldDelete) {

					shouldDelete = true;

					for (File targetFile : aTargetFiles) {
						if (file.getAbsolutePath().startsWith(targetFile.getAbsolutePath())) {
							shouldDelete = false;
							break;
						}
					}
				}

				if (shouldDelete) {

					itemsToDelete.add(songFile.getId());

					log.debug("song file deleted: {}", songFile);
				}

				if (aHandler != null) {
					aHandler.handleProgress((double) processedItems / page.getTotalElements());
				}

				processedItems++;
			}

			Pageable nextPageable = page.nextPageable();

			page = nextPageable != null ? songFileService.getAll(nextPageable) : null;

		} while (page != null);

		for (Integer id : itemsToDelete) {
			songService.deleteByFileId(id);
			songFileService.deleteById(id);
		}

		if (itemsToDelete.size() > 0) {
			log.info("deleted {} songs", itemsToDelete.size());
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void normalizeStoredFiles(ProgressHandler aHandler) {

		List<Integer> itemsToDelete = new ArrayList<Integer>();

		long processedItems = 0;

		Page<StoredFile> page = storedFileService.getByTag(FILE_TAG_ARTWORK, new PageRequest(0, CLEANING_BUFFER_SIZE, Sort.Direction.ASC, "id"));

		do {

			for (StoredFile storedFile : page.getContent()) {

				if (songFileService.getCountByArtwork(storedFile.getId()) == 0) {

					itemsToDelete.add(storedFile.getId());

					log.debug("stored file deleted: {}", storedFile);
				}

				if (aHandler != null) {
					aHandler.handleProgress((double) processedItems / page.getTotalElements());
				}

				processedItems++;
			}

			Pageable nextPageable = page.nextPageable();

			page = nextPageable != null ? storedFileService.getByTag(FILE_TAG_ARTWORK, nextPageable) : null;

		} while (page != null);

		for (Integer id : itemsToDelete) {

			for (Album album : albumService.getByArtwork(id)) {

				album.setArtwork(null);

				albumService.save(album);
			}
			for (Artist artist : artistService.getByArtwork(id)) {

				artist.setArtwork(null);

				artistService.save(artist);
			}

			storedFileService.deleteById(id);
		}

		if (itemsToDelete.size() > 0) {
			log.info("deleted {} stored files", itemsToDelete.size());
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void normalizeAlbums(ProgressHandler aHandler) {

		List<Integer> itemsToDelete = new ArrayList<Integer>();

		long processedItems = 0;
		long updatedArtworks = 0;

		Page<Album> page = albumService.getAll(new PageRequest(0, CLEANING_BUFFER_SIZE, Sort.Direction.ASC, "id"));

		do {

			for (Album album : page.getContent()) {

				if (songService.getCountByAlbum(album.getId()) == 0) {

					itemsToDelete.add(album.getId());

					log.debug("album deleted: {}", album);

				} else if (album.getArtwork() == null) {

					for (Song song : album.getSongs()) {

						if (song.getFile() != null && song.getFile().getArtwork() != null) {

							album.setArtwork(song.getFile().getArtwork());

							albumService.save(album);

							updatedArtworks++;

							break;
						}
					}
				}

				if (aHandler != null) {
					aHandler.handleProgress((double) processedItems / page.getTotalElements());
				}

				processedItems++;
			}

			Pageable nextPageable = page.nextPageable();

			page = nextPageable != null ? albumService.getAll(nextPageable) : null;

		} while (page != null);

		for (Integer id : itemsToDelete) {
			albumService.deleteById(id);
		}

		if (itemsToDelete.size() > 0) {
			log.info("deleted {} albums", itemsToDelete.size());
		}
		if (updatedArtworks > 0) {
			log.info("updated artworks of {} albums", updatedArtworks);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void normalizeArtists(ProgressHandler aHandler) {

		List<Integer> itemsToDelete = new ArrayList<Integer>();

		long processedItems = 0;
		long updatedArtworks = 0;

		Page<Artist> page = artistService.getAll(new PageRequest(0, CLEANING_BUFFER_SIZE, Sort.Direction.ASC, "id"));

		do {

			for (Artist artist : page.getContent()) {

				if (albumService.getCountByArtist(artist.getId()) == 0) {

					itemsToDelete.add(artist.getId());

					log.debug("artist deleted: {}", artist);

				} else if (artist.getArtwork() == null) {

					List<Album> albumsWithArtwork = new ArrayList<Album>();

					for (Album album : artist.getAlbums()) {
						if (album.getArtwork() != null) {
							albumsWithArtwork.add(album);
						}
					}

					if (albumsWithArtwork.size() > 0) {

						Album album = albumsWithArtwork.get((int)Math.floor(albumsWithArtwork.size() / 2.0));

						artist.setArtwork(album.getArtwork());

						artistService.save(artist);

						updatedArtworks++;
					}
				}

				if (aHandler != null) {
					aHandler.handleProgress((double) processedItems / page.getTotalElements());
				}

				processedItems++;
			}

			Pageable nextPageable = page.nextPageable();

			page = nextPageable != null ? artistService.getAll(nextPageable) : null;

		} while (page != null);

		for (Integer id : itemsToDelete) {
			artistService.deleteById(id);
		}

		if (itemsToDelete.size() > 0) {
			log.info("deleted {} artists", itemsToDelete.size());
		}
		if (updatedArtworks > 0) {
			log.info("updated artworks of {} artists", updatedArtworks);
		}
	}

	private EntityModification<SongFile> importSongFile(SongData aSongData) {

		boolean shouldSave = false;

		StoredFile storedFile = null;

		if (aSongData.getArtwork() != null && aSongData.getArtwork().getChecksum() != null) {

			storedFile = storedFileService.getByTagAndChecksum(FILE_TAG_ARTWORK, aSongData.getArtwork().getChecksum());

			if (storedFile == null) {

				try {

					StorageTask storageTask = songDataToArtworkStorageTask(aSongData);

					storedFile = storedFileService.save(storageTask);

					log.debug("artwork stored {}", storedFile);

				} catch (Exception e) {
					log.warn("could not store artwork", e);
				}

				shouldSave = true;
			}
		}

		SongFile songFile = songFileService.getByPath(aSongData.getPath());

		if (songFile == null) {

			songFile = new SongFile();

			songFile.setPath(aSongData.getPath());

			shouldSave = true;
		}

		if (songFile.getId() != null) {

			if (!ObjectUtils.nullSafeEquals(songFile.getFormat(), aSongData.getFormat()) ||
				!ObjectUtils.nullSafeEquals(songFile.getMimeType(), aSongData.getMimeType()) ||
				!ObjectUtils.nullSafeEquals(songFile.getSize(), aSongData.getSize()) ||
				!ObjectUtils.nullSafeEquals(songFile.getDuration(), aSongData.getDuration()) ||
				!ObjectUtils.nullSafeEquals(songFile.getBitRate(), aSongData.getBitRate()) ||

				!ObjectUtils.nullSafeEquals(songFile.getDiscNumber(), aSongData.getDiscNumber()) ||
				!ObjectUtils.nullSafeEquals(songFile.getDiscCount(), aSongData.getDiscCount()) ||

				!ObjectUtils.nullSafeEquals(songFile.getTrackNumber(), aSongData.getTrackNumber()) ||
				!ObjectUtils.nullSafeEquals(songFile.getTrackCount(), aSongData.getTrackCount()) ||

				!ObjectUtils.nullSafeEquals(songFile.getName(), aSongData.getName()) ||
				!ObjectUtils.nullSafeEquals(songFile.getArtist(), aSongData.getArtist()) ||
				!ObjectUtils.nullSafeEquals(songFile.getAlbum(), aSongData.getAlbum()) ||
				!ObjectUtils.nullSafeEquals(songFile.getYear(), aSongData.getYear()) ||
				!ObjectUtils.nullSafeEquals(songFile.getArtwork(), storedFile)) {

				shouldSave = true;
			}
		}

		if (shouldSave) {

			songFile.setFormat(aSongData.getFormat());
			songFile.setMimeType(aSongData.getMimeType());
			songFile.setSize(aSongData.getSize());
			songFile.setDuration(aSongData.getDuration());
			songFile.setBitRate(aSongData.getBitRate());

			songFile.setDiscNumber(aSongData.getDiscNumber());
			songFile.setDiscCount(aSongData.getDiscCount());

			songFile.setTrackNumber(aSongData.getTrackNumber());
			songFile.setTrackCount(aSongData.getTrackCount());

			songFile.setName(aSongData.getName());
			songFile.setArtist(aSongData.getArtist());
			songFile.setAlbum(aSongData.getAlbum());
			songFile.setYear(aSongData.getYear());

			songFile.setArtwork(storedFile);

			songFile = songFileService.save(songFile);
		}

		return new EntityModification<SongFile>(songFile, shouldSave);
	}

	private EntityModification<Artist> importArtist(SongFile aSongFile) {

		Artist artist = artistService.getByName(aSongFile.getArtist());

		if (artist == null) {

			artist = new Artist();

			artist.setName(aSongFile.getArtist());

			artist = artistService.save(artist);
		}

		return new EntityModification<Artist>(artist, artist == null);
	}

	private EntityModification<Album> importAlbum(SongFile aSongFile, Artist aArtist) {

		Album album = albumService.getByArtistAndName(aArtist.getId(), aSongFile.getAlbum());

		boolean shouldSave = false;

		if (album == null) {

			album = new Album();

			album.setName(aSongFile.getAlbum());
			album.setArtist(aArtist);

			shouldSave = true;
		}

		if (album.getId() != null) {
			if (!ObjectUtils.nullSafeEquals(album.getYear(), aSongFile.getYear()) ||
				!ObjectUtils.nullSafeEquals(album.getDiscCount(), aSongFile.getDiscCount()) ||
				!ObjectUtils.nullSafeEquals(album.getTrackCount(), aSongFile.getTrackCount())) {

				shouldSave = true;
			}
		}

		if (shouldSave) {

			album.setYear(aSongFile.getYear());
			album.setDiscCount(aSongFile.getDiscCount());
			album.setTrackCount(aSongFile.getTrackCount());

			album = albumService.save(album);
		}

		return new EntityModification<Album>(album, shouldSave);
	}

	private EntityModification<Song> importSong(SongFile aSongFile, Album aAlbum) {

		Song song = songService.getByFile(aSongFile.getId());

		boolean shouldSave = false;

		if (song == null) {

			song = new Song();
			song.setFile(aSongFile);

			shouldSave = true;
		}

		if (song.getId() != null) {
			if (!ObjectUtils.nullSafeEquals(song.getAlbum(), aAlbum)) {
				shouldSave = true;
			}
		}

		if (shouldSave) {

			song.setAlbum(aAlbum);

			song = songService.save(song);
		}

		return new EntityModification<Song>(song, shouldSave);
	}

	private StorageTask songDataToArtworkStorageTask(SongData aSongData) throws IOException {

		File createdFile = new File(FileUtils.getTempDirectory(), "pony.artwork." + UUID.randomUUID() + ".tmp");

		FileUtils.writeByteArrayToFile(createdFile, aSongData.getArtwork().getBinaryData());

		StorageTask storageTask = new StorageTask(StorageTask.Type.MOVE, createdFile);

		storageTask.setName(aSongData.getArtist() + " " + aSongData.getAlbum() + " " + aSongData.getName());
		storageTask.setMimeType(aSongData.getArtwork().getMimeType());
		storageTask.setChecksum(aSongData.getArtwork().getChecksum());
		storageTask.setTag(FILE_TAG_ARTWORK);

		return storageTask;
	}

	private static class EntityModification<T extends AbstractEntity> {

		private T entity;

		private boolean modified;

		private EntityModification(T aEntity, boolean aModified) {
			entity = aEntity;
			modified = aModified;
		}

		public T getEntity() {
			return entity;
		}

		public boolean isModified() {
			return modified;
		}
	}
}
