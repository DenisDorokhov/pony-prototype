package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.domain.*;
import net.dorokhov.pony.core.service.*;
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
import java.util.HashSet;
import java.util.Set;

@Service
public class LibraryServiceImpl implements LibraryService {

	private static final int CLEANING_BUFFER_SIZE = 300;

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

								log.error("could not create song entities for song file: {}", songFile, e);

								throw new RuntimeException(e);
							}

						} else {
							log.warn("could not create song entities for inconsistent song file: {}", songFile);
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

		log.debug("cleaning albums...");
		cleanAlbums();

		log.debug("cleaning artists...");
		cleanArtists();
	}

	private EntityModification<SongFile> importSongFile(SongData aMetaData) {

		SongFile songFile = songFileService.getByPath(aMetaData.getPath());

		boolean shouldSave = false;

		if (songFile == null) {

			songFile = new SongFile();

			songFile.setPath(aMetaData.getPath());

			shouldSave = true;
		}

		if (songFile.getId() != null) {
			if (!ObjectUtils.nullSafeEquals(songFile.getFormat(), aMetaData.getFormat()) ||
				!ObjectUtils.nullSafeEquals(songFile.getMimeType(), aMetaData.getMimeType()) ||
				!ObjectUtils.nullSafeEquals(songFile.getSize(), aMetaData.getSize()) ||
				!ObjectUtils.nullSafeEquals(songFile.getDuration(), aMetaData.getDuration()) ||
				!ObjectUtils.nullSafeEquals(songFile.getBitRate(), aMetaData.getBitRate()) ||

				!ObjectUtils.nullSafeEquals(songFile.getDiscNumber(), aMetaData.getDiscNumber()) ||
				!ObjectUtils.nullSafeEquals(songFile.getDiscCount(), aMetaData.getDiscCount()) ||

				!ObjectUtils.nullSafeEquals(songFile.getTrackNumber(), aMetaData.getTrackNumber()) ||
				!ObjectUtils.nullSafeEquals(songFile.getTrackCount(), aMetaData.getTrackCount()) ||

				!ObjectUtils.nullSafeEquals(songFile.getName(), aMetaData.getName()) ||
				!ObjectUtils.nullSafeEquals(songFile.getArtist(), aMetaData.getArtist()) ||
				!ObjectUtils.nullSafeEquals(songFile.getAlbum(), aMetaData.getAlbum()) ||
				!ObjectUtils.nullSafeEquals(songFile.getYear(), aMetaData.getYear())) {

				shouldSave = true;
			}
		}

		if (shouldSave) {

			songFile.setFormat(aMetaData.getFormat());
			songFile.setMimeType(aMetaData.getMimeType());
			songFile.setSize(aMetaData.getSize());
			songFile.setDuration(aMetaData.getDuration());
			songFile.setBitRate(aMetaData.getBitRate());

			songFile.setDiscNumber(aMetaData.getDiscNumber());
			songFile.setDiscCount(aMetaData.getDiscCount());

			songFile.setTrackNumber(aMetaData.getTrackNumber());
			songFile.setTrackCount(aMetaData.getTrackCount());

			songFile.setName(aMetaData.getName());
			songFile.setArtist(aMetaData.getArtist());
			songFile.setAlbum(aMetaData.getAlbum());
			songFile.setYear(aMetaData.getYear());

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

		Set<Integer> songFilesToDelete = new HashSet<Integer>();

		Page<SongFile> songFiles = songFileService.getAll(new PageRequest(0, CLEANING_BUFFER_SIZE, Sort.Direction.ASC, "path"));

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

	private void cleanAlbums() {

		Set<Integer> albumsToDelete = new HashSet<Integer>();

		Page<Album> albums = albumService.getAll(new PageRequest(0, CLEANING_BUFFER_SIZE, Sort.Direction.ASC, "name"));

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

		Set<Integer> artistsToDelete = new HashSet<Integer>();

		Page<Artist> artists = artistService.getAll(new PageRequest(0, CLEANING_BUFFER_SIZE, Sort.Direction.ASC, "name"));

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
