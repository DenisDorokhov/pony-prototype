package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.entity.Album;
import net.dorokhov.pony.core.entity.Artist;
import net.dorokhov.pony.core.entity.Song;
import net.dorokhov.pony.core.entity.SongFile;
import net.dorokhov.pony.core.service.*;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LibraryServiceImpl implements LibraryService {

	private static final int SONG_FILE_BUFFER_SIZE = 100;

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final Object lock = new Object();

	private TransactionTemplate transactionTemplate;

	private SongFileService songFileService;

	private ArtistService artistService;

	private AlbumService albumService;

	private SongService songService;

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

	@Override
	public SongFile importSongFile(final File aFile) {

		SongFile songFile = songFileService.getByPath(aFile.getAbsolutePath());

		if (songFile == null || songFile.getUpdateDate().getTime() < aFile.lastModified()) {

			final AudioFile audioFile;

			try {
				audioFile = AudioFileIO.read(aFile);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			synchronized (lock) {

				songFile = transactionTemplate.execute(new TransactionCallback<SongFile>() {

					@Override
					public SongFile doInTransaction(TransactionStatus status) {

						SongFile songFile;

						try {
							songFile = importFile(audioFile);
						} catch (Exception e) {

							log.error("could not import song file: {}", aFile.getAbsolutePath(), e);

							throw new RuntimeException(e);
						}

						if (songFile != null && songFile.getName() != null && songFile.getArtist() != null && songFile.getAlbum() != null) {

							try {

								Artist artist = importArtist(songFile);
								Album album = importAlbum(songFile, artist);
								Song song = importSong(songFile, album);

								log.debug("song imported: {}", song);

							} catch (Exception e) {

								log.error("could not create song entities for song file: {}", songFile, e);

								throw new RuntimeException(e);
							}

						} else {
							log.warn("could not create song entities for inconsistent song file: {}", songFile);
						}

						return songFile;
					}
				});
			}
		}

		return songFile;
	}

	@Override
	@Transactional
	public void cleanUpSongFiles() {

		List<Integer> songFileIds = new ArrayList<Integer>();
		Set<Integer> albumIds = new HashSet<Integer>();

		Page<SongFile> songFiles = songFileService.getAll(new PageRequest(0, SONG_FILE_BUFFER_SIZE, Sort.Direction.ASC, "path"));

		do {

			for (SongFile songFile : songFiles.getContent()) {

				File file = new File(songFile.getPath());

				if (!file.exists()) {

					songFileIds.add(songFile.getId());

					log.debug("song file deleted: {}", songFile);

					Song song = songService.getByFile(songFile.getId());

					if (song != null) {

						albumIds.add(song.getAlbum().getId());

						songService.deleteById(song.getId());

						log.debug("song deleted: {}", song);
					}
				}
			}

			Pageable nextPageable = songFiles.nextPageable();

			songFiles = nextPageable != null ? songFileService.getAll(nextPageable) : null;

		} while (songFiles != null);

		for (Integer id : songFileIds) {
			songFileService.deleteById(id);
		}

		for (Integer id : albumIds) {

			Album album = albumService.getById(id);

			if (album != null) {

				Artist artist = album.getArtist();

				if (songService.getCountByAlbum(id) == 0) {

					albumService.deleteById(id);

					log.debug("album deleted: {}", album);
				}

				if (artist != null && songService.getCountByArtist(artist.getId()) == 0) {

					artistService.deleteById(artist.getId());

					log.debug("artist deleted: {}", album);
				}
			}
		}
	}

	private SongFile importFile(AudioFile aFile) {

		AudioHeader header = aFile.getAudioHeader();
		Tag tag = aFile.getTag();

		SongFile songFile = songFileService.getByPath(aFile.getFile().getAbsolutePath());

		if (songFile == null) {
			songFile = new SongFile();
		}

		songFile.setPath(aFile.getFile().getAbsolutePath());
		songFile.setType(header.getFormat());
		songFile.setSize(aFile.getFile().length());

		songFile.setDuration(header.getTrackLength());
		songFile.setBitRate(header.getBitRateAsNumber());

		songFile.setDiscNumber(parseIntegerTag(tag, FieldKey.DISC_NO));
		songFile.setDiscCount(parseIntegerTag(tag, FieldKey.DISC_TOTAL));

		songFile.setTrackNumber(parseIntegerTag(tag, FieldKey.TRACK));
		songFile.setTrackCount(parseIntegerTag(tag, FieldKey.TRACK_TOTAL));

		songFile.setName(parseStringTag(tag, FieldKey.TITLE));
		songFile.setAlbum(parseStringTag(tag, FieldKey.ALBUM));
		songFile.setArtist(parseStringTag(tag, FieldKey.ARTIST));

		songFile.setYear(parseIntegerTag(tag, FieldKey.YEAR));

		songFile = songFileService.save(songFile);

		return songFile;
	}

	private Artist importArtist(SongFile aSongFile) {

		Artist artist = artistService.getByName(aSongFile.getArtist());

		if (artist == null) {

			artist = new Artist();
			artist.setName(aSongFile.getArtist());
		}

		return artistService.save(artist);
	}

	public Album importAlbum(SongFile aSongFile, Artist aArtist) {

		Album album = albumService.getByArtistAndName(aArtist.getId(), aSongFile.getAlbum());

		if (album == null) {

			album = new Album();
			album.setName(aSongFile.getAlbum());
			album.setArtist(aArtist);
		}

		if (album.getYear() == null) {
			album.setYear(aSongFile.getYear());
		}
		if (album.getDiscCount() == null) {
			album.setDiscCount(aSongFile.getDiscCount());
		}
		if (album.getTrackCount() == null) {
			album.setTrackCount(aSongFile.getTrackCount());
		}

		return albumService.save(album);
	}

	private Song importSong(SongFile aSongFile, Album aAlbum) {

		Song song = songService.getByFile(aSongFile.getId());

		if (song == null) {

			song = new Song();
			song.setFile(aSongFile);
		}

		song.setAlbum(aAlbum);

		return songService.save(song);
	}

	private String parseStringTag(Tag aTag, FieldKey aKey) {

		String result = aTag.getFirst(aKey);

		if (result != null) {

			result = result.trim();

			if (result.length() == 0) {
				result = null;
			}
		}

		return result;
	}

	private Integer parseIntegerTag(Tag aTag, FieldKey aKey) {

		Integer result = null;

		try {
			result = Integer.valueOf(aTag.getFirst(aKey));
		} catch (NumberFormatException e) {}

		return result;
	}
}
