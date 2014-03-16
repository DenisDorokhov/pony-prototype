package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.domain.*;
import net.dorokhov.pony.core.service.*;
import org.apache.commons.codec.digest.DigestUtils;
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
import java.util.ArrayList;
import java.util.List;

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
	public SongFile importSongFile(File aFile) {

		SongFile songFile = songFileService.getByPath(aFile.getAbsolutePath());

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

	@Override
	@Transactional
	public void clean() {

		log.debug("cleaning songs...");
		cleanSongs();

		log.debug("cleaning stored files...");
		cleanFiles();

		log.debug("cleaning albums...");
		cleanAlbums();

		log.debug("cleaning artists...");
		cleanArtists();
	}

	private EntityModification<SongFile> importSongFile(SongData aSongData) {

		boolean shouldSave = false;

		String checksum = null;
		if (aSongData.getArtwork() != null) {
			checksum = DigestUtils.md5Hex(aSongData.getArtwork().getBinaryData());
		}

		StoredFile storedFile = null;

		if (checksum != null) {

			storedFile = storedFileService.getByTagAndChecksum(FILE_TAG_ARTWORK, checksum);

			if (storedFile == null) {

				try {

					String contentName = aSongData.getArtist() + " " + aSongData.getAlbum() + " " + aSongData.getName();

					StorageTask storageTask = StorageTask.createWithTemporaryFile(aSongData.getArtwork().getBinaryData(), contentName);

					storageTask.setMimeType(aSongData.getArtwork().getMimeType());
					storageTask.setChecksum(checksum);
					storageTask.setTag(FILE_TAG_ARTWORK);

					storedFile = storedFileService.save(storageTask);

					log.debug("stored not-existing artwork {}", storedFile);

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

	private void cleanSongs() {

		List<Integer> songFilesToDelete = new ArrayList<Integer>();

		Page<SongFile> songFiles = songFileService.getAll(new PageRequest(0, CLEANING_BUFFER_SIZE, Sort.Direction.ASC, "id"));

		do {

			for (SongFile songFile : songFiles.getContent()) {

				File file = new File(songFile.getPath());

				if (!file.exists()) {

					songFilesToDelete.add(songFile.getId());

					log.debug("song file deleted: {}", songFile);

					Song song = songService.getByFile(songFile.getId());

					if (song != null) {

						songService.deleteById(song.getId());

						log.debug("song deleted: {}", song);
					}
				}
			}

			Pageable nextPageable = songFiles.nextPageable();

			songFiles = nextPageable != null ? songFileService.getAll(nextPageable) : null;

		} while (songFiles != null);

		for (Integer id : songFilesToDelete) {
			songFileService.deleteById(id);
		}
	}

	private void cleanFiles() {

		List<Integer> storedFilesToDelete = new ArrayList<Integer>();

		Page<StoredFile> storedFiles = storedFileService.getByTag(FILE_TAG_ARTWORK, new PageRequest(0, CLEANING_BUFFER_SIZE, Sort.Direction.ASC, "id"));

		do {

			for (StoredFile storedFile : storedFiles.getContent()) {
				if (songFileService.getCountByArtwork(storedFile.getId()) == 0) {

					storedFilesToDelete.add(storedFile.getId());

					log.debug("file deleted: {}", storedFile);
				}
			}

			Pageable nextPageable = storedFiles.nextPageable();

			storedFiles = nextPageable != null ? storedFileService.getByTag(FILE_TAG_ARTWORK, nextPageable) : null;

		} while (storedFiles != null);

		for (Integer id : storedFilesToDelete) {
			storedFileService.deleteById(id);
		}
	}

	private void cleanAlbums() {

		List<Integer> albumsToDelete = new ArrayList<Integer>();

		Page<Album> albums = albumService.getAll(new PageRequest(0, CLEANING_BUFFER_SIZE, Sort.Direction.ASC, "id"));

		do {

			for (Album album : albums.getContent()) {
				if (songService.getCountByAlbum(album.getId()) == 0) {

					albumsToDelete.add(album.getId());

					log.debug("album deleted: {}", album);
				}
			}

			Pageable nextPageable = albums.nextPageable();

			albums = nextPageable != null ? albumService.getAll(nextPageable) : null;

		} while (albums != null);

		for (Integer id : albumsToDelete) {
			albumService.deleteById(id);
		}
	}

	private void cleanArtists() {

		List<Integer> artistsToDelete = new ArrayList<Integer>();

		Page<Artist> artists = artistService.getAll(new PageRequest(0, CLEANING_BUFFER_SIZE, Sort.Direction.ASC, "id"));

		do {

			for (Artist artist : artists.getContent()) {
				if (albumService.getCountByArtist(artist.getId()) == 0) {

					artistsToDelete.add(artist.getId());

					log.debug("artist deleted: {}", artist);
				}
			}

			Pageable nextPageable = artists.nextPageable();

			artists = nextPageable != null ? artistService.getAll(nextPageable) : null;

		} while (artists != null);

		for (Integer id : artistsToDelete) {
			artistService.deleteById(id);
		}
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
