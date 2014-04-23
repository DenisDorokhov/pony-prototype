package net.dorokhov.pony.core.service.library;

import net.dorokhov.pony.core.domain.*;
import net.dorokhov.pony.core.dao.entity.BaseEntity;
import net.dorokhov.pony.core.service.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.util.UUID;

@Service
public class LibraryImporterImpl implements LibraryImporter {

	private static final String FILE_TAG_ARTWORK_INTERNAL = "artworkEmbedded";

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final Object lock = new Object();

	private TransactionTemplate transactionTemplate;

	private SongFileService songFileService;

	private ArtistService artistService;

	private AlbumService albumService;

	private SongService songService;

	private StoredFileService storedFileService;

	private SongDataReader songDataReader;

	private ThumbnailService thumbnailService;

	@Autowired
	public void setTransactionManager(PlatformTransactionManager aTransactionManager) {
		transactionTemplate = new TransactionTemplate(aTransactionManager, new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
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

	@Autowired
	public void setThumbnailService(ThumbnailService aThumbnailService) {
		thumbnailService = aThumbnailService;
	}

	@Override
	@Transactional
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

	private EntityModification<SongFile> importSongFile(SongData aSongData) {

		boolean shouldSave = false;

		StoredFile storedFile = null;

		if (aSongData.getArtwork() != null && aSongData.getArtwork().getChecksum() != null) {

			storedFile = storedFileService.getByTagAndChecksum(FILE_TAG_ARTWORK_INTERNAL, aSongData.getArtwork().getChecksum());

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
				!ObjectUtils.nullSafeEquals(songFile.getAlbumArtist(), aSongData.getAlbumArtist()) ||
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
			songFile.setAlbumArtist(aSongData.getAlbumArtist());
			songFile.setAlbum(aSongData.getAlbum());
			songFile.setYear(aSongData.getYear());

			songFile.setArtwork(storedFile);

			songFile = songFileService.save(songFile);
		}

		return new EntityModification<SongFile>(songFile, shouldSave);
	}

	private EntityModification<Artist> importArtist(SongFile aSongFile) {

		String artistName = aSongFile.getAlbumArtist();

		if (artistName == null) {
			artistName = aSongFile.getArtist();
		}

		Artist artist = artistService.getByName(artistName);

		if (artist == null) {

			artist = new Artist();

			artist.setName(artistName);

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
			if (!ObjectUtils.nullSafeEquals(album.getYear(), aSongFile.getYear())) {
				shouldSave = true;
			}
		}

		if (shouldSave) {

			album.setYear(aSongFile.getYear());

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

	private StorageTask songDataToArtworkStorageTask(SongData aSongData) throws Exception {

		File file = new File(FileUtils.getTempDirectory(), "pony." + FILE_TAG_ARTWORK_INTERNAL + "." + UUID.randomUUID() + ".tmp");

		thumbnailService.makeThumbnail(aSongData.getArtwork().getBinaryData(), file);

		StorageTask storageTask = new StorageTask(StorageTask.Type.MOVE, file);

		storageTask.setName(aSongData.getArtist() + " " + aSongData.getAlbum() + " " + aSongData.getName());
		storageTask.setMimeType(aSongData.getArtwork().getMimeType());
		storageTask.setChecksum(aSongData.getArtwork().getChecksum());
		storageTask.setTag(FILE_TAG_ARTWORK_INTERNAL);

		return storageTask;
	}

	private static class EntityModification<T extends BaseEntity> {

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
