package net.dorokhov.pony.core.service.library;

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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

@Service
public class LibraryNormalizerImpl implements LibraryNormalizer {

	private static final int CLEANING_BUFFER_SIZE = 300;

	private static final String FILE_TAG_ARTWORK_INTERNAL = "artworkEmbedded";
	private static final String FILE_TAG_ARTWORK_EXTERNAL = "artworkExternal";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private SongFileService songFileService;

	private ArtistService artistService;

	private AlbumService albumService;

	private SongService songService;

	private StoredFileService storedFileService;

	private ArtworkService artworkService;

	private ChecksumService checksumService;

	private MimeTypeService mimeTypeService;

	private ThumbnailService thumbnailService;

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
	public void setArtworkService(ArtworkService aArtworkService) {
		artworkService = aArtworkService;
	}

	@Autowired
	public void setChecksumService(ChecksumService aChecksumService) {
		checksumService = aChecksumService;
	}

	@Autowired
	public void setMimeTypeService(MimeTypeService aMimeTypeService) {
		mimeTypeService = aMimeTypeService;
	}

	@Autowired
	public void setThumbnailService(ThumbnailService aThumbnailService) {
		thumbnailService = aThumbnailService;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void normalizeSongs(List<File> aTargetFiles, LibraryNormalizer.ProgressHandler aHandler) {

		List<Long> itemsToDelete = new ArrayList<Long>();

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

		for (Long id : itemsToDelete) {
			songService.deleteByFileId(id);
			songFileService.deleteById(id);
		}

		if (itemsToDelete.size() > 0) {
			log.info("deleted {} songs", itemsToDelete.size());
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void normalizeStoredFiles(ProgressHandler aHandler) {

		List<Long> itemsToDelete = new ArrayList<Long>();

		long processedItems = 0;

		Page<StoredFile> pageInternal = storedFileService.getByTag(FILE_TAG_ARTWORK_INTERNAL, new PageRequest(0, CLEANING_BUFFER_SIZE, Sort.Direction.ASC, "id"));
		Page<StoredFile> pageExternal = storedFileService.getByTag(FILE_TAG_ARTWORK_EXTERNAL, new PageRequest(0, CLEANING_BUFFER_SIZE, Sort.Direction.ASC, "id"));

		long totalElements = pageInternal.getTotalElements() + pageExternal.getTotalElements();

		do {

			for (StoredFile storedFile : pageInternal.getContent()) {

				if (songFileService.getCountByArtwork(storedFile.getId()) == 0) {

					itemsToDelete.add(storedFile.getId());

					log.debug("internal artwork file deleted: {}", storedFile);
				}

				if (aHandler != null) {
					aHandler.handleProgress((double) processedItems / totalElements);
				}

				processedItems++;
			}

			Pageable nextPageable = pageInternal.nextPageable();

			pageInternal = nextPageable != null ? storedFileService.getByTag(FILE_TAG_ARTWORK_INTERNAL, nextPageable) : null;

		} while (pageInternal != null);

		do {

			for (StoredFile storedFile : pageExternal.getContent()) {

				File externalFile = null;

				if (storedFile.getUserData() != null) {
					externalFile = new File(storedFile.getUserData());
				}

				if (externalFile == null || !externalFile.exists() ||
						(albumService.getCountByArtwork(storedFile.getId()) == 0 && artistService.getCountByArtwork(storedFile.getId()) == 0)) {

					itemsToDelete.add(storedFile.getId());

					log.debug("external artwork file deleted: {}", storedFile);
				}

				if (aHandler != null) {
					aHandler.handleProgress((double) processedItems / totalElements);
				}

				processedItems++;
			}

			Pageable nextPageable = pageExternal.nextPageable();

			pageExternal = nextPageable != null ? storedFileService.getByTag(FILE_TAG_ARTWORK_EXTERNAL, nextPageable) : null;

		} while (pageExternal != null);

		for (Long id : itemsToDelete) {

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

		List<Long> itemsToDelete = new ArrayList<Long>();

		long processedItems = 0;
		long updatedArtworks = 0;
		long updatedYears = 0;

		Page<Album> page = albumService.getAll(new PageRequest(0, CLEANING_BUFFER_SIZE, Sort.Direction.ASC, "id"));

		do {

			for (Album album : page.getContent()) {

				if (songService.getCountByAlbum(album.getId()) == 0) {

					itemsToDelete.add(album.getId());

					log.debug("album deleted: {}", album);

				} else {

					boolean shouldSave = false;

					if (album.getArtwork() == null) {

						try {
							fetchAlbumArtwork(album);
						} catch (Exception e) {
							log.error("could not fetch external artwork for album {}", album, e);
						}

						if (album.getArtwork() != null) {
							updatedArtworks++;
							shouldSave = true;
						}
					}

					if (album.getYear() == null) {

						fetchAlbumYear(album);

						if (album.getYear() != null) {
							updatedYears++;
							shouldSave = true;
						}
					}

					if (shouldSave) {
						albumService.save(album);
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

		for (Long id : itemsToDelete) {
			albumService.deleteById(id);
		}

		if (itemsToDelete.size() > 0) {
			log.info("deleted {} albums", itemsToDelete.size());
		}
		if (updatedArtworks > 0) {
			log.info("updated artwork of {} albums", updatedArtworks);
		}
		if (updatedYears > 0) {
			log.info("updated year of {} albums", updatedYears);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void normalizeArtists(ProgressHandler aHandler) {

		List<Long> itemsToDelete = new ArrayList<Long>();

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

						Collections.sort(albumsWithArtwork);

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

		for (Long id : itemsToDelete) {
			artistService.deleteById(id);
		}

		if (itemsToDelete.size() > 0) {
			log.info("deleted {} artists", itemsToDelete.size());
		}
		if (updatedArtworks > 0) {
			log.info("updated artworks of {} artists", updatedArtworks);
		}
	}

	private void fetchAlbumArtwork(Album aAlbum) throws Exception {

		List<Song> songList = aAlbum.getSongs();

		if (aAlbum.getArtwork() == null) {
			for (Song song : songList) {

				if (song.getFile() != null && song.getFile().getArtwork() != null) {
					aAlbum.setArtwork(song.getFile().getArtwork());
				}

				if (aAlbum.getArtwork() != null) {
					break;
				}
			}
		}

		if (aAlbum.getArtwork() == null) {

			Set<String> discoveredPaths = new HashSet<String>();

			for (Song song : songList) {
				if (song.getFile() != null) {

					File folder = new File(song.getFile().getPath()).getParentFile();

					if (folder != null && !discoveredPaths.contains(folder.getAbsolutePath())) {

						fetchAlbumArtwork(aAlbum, song, folder);

						if (aAlbum.getArtwork() != null) {
							break;
						}

						discoveredPaths.add(folder.getAbsolutePath());
					}
				}
			}
		}
	}

	private void fetchAlbumArtwork(Album aAlbum, Song aSong, File aFolder) throws Exception {

		File artworkFile = artworkService.discoverArtwork(aFolder);

		if (artworkFile != null) {

			String checksum = checksumService.calculateChecksum(artworkFile);

			StoredFile storedFile = storedFileService.getByTagAndChecksum(FILE_TAG_ARTWORK_EXTERNAL, checksum);

			if (storedFile == null) {

				String mimeType = mimeTypeService.getFileMimeType(artworkFile);

				if (mimeType != null) {

					File file = new File(FileUtils.getTempDirectory(), "pony." + FILE_TAG_ARTWORK_EXTERNAL + "." + UUID.randomUUID() + ".tmp");

					thumbnailService.makeThumbnail(artworkFile, file);

					StorageTask storageTask = new StorageTask(StorageTask.Type.MOVE, file);

					storageTask.setName(aSong.getFile().getArtist() + " " + aSong.getFile().getAlbum() + " " + aSong.getFile().getName());
					storageTask.setMimeType(mimeType);
					storageTask.setChecksum(checksum);
					storageTask.setTag(FILE_TAG_ARTWORK_EXTERNAL);
					storageTask.setUserData(artworkFile.getAbsolutePath());

					storedFile = storedFileService.save(storageTask);

					log.debug("external artwork stored {}", storedFile);
				}
			}

			if (storedFile != null) {
				aAlbum.setArtwork(storedFile);
			}
		}
	}

	private void fetchAlbumYear(Album aAlbum) {

		for (Song song : aAlbum.getSongs()) {
			if (song.getFile() != null && song.getFile().getYear() != null) {

				aAlbum.setYear(song.getFile().getYear());

				log.debug("year of album {} set by song {}", aAlbum, song);

				break;
			}
		}
	}

}
