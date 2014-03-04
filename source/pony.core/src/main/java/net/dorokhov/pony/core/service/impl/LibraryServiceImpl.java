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
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.File;
import java.util.Date;

@Service
public class LibraryServiceImpl implements LibraryService {

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
	public SongFile importSongFile(File aFile) {

		final AudioFile audioFile;

		try {
			audioFile = AudioFileIO.read(aFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		synchronized (lock) {

			return transactionTemplate.execute(new TransactionCallback<SongFile>() {

				@Override
				public SongFile doInTransaction(TransactionStatus status) {

					SongFile songFile = importFile(audioFile);

					if (songFile.getName() != null && songFile.getArtist() != null && songFile.getAlbum() != null) {

						try {

							Artist artist = importArtist(songFile);
							Album album = importAlbum(songFile, artist);

							importSong(songFile, album);

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

	@Override
	@Transactional
	public void clearSongFilesImportedBefore(Date aDate) {

		artistService.deleteUpdatedBefore(aDate);
		albumService.deleteUpdatedBefore(aDate);
		songService.deleteUpdatedBefore(aDate);

		songFileService.deleteUpdatedBefore(aDate);
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

		songFile.setDiscNumber(parseIntTag(tag, FieldKey.DISC_NO));
		songFile.setDiscCount(parseIntTag(tag, FieldKey.DISC_TOTAL));

		songFile.setTrackNumber(parseIntTag(tag, FieldKey.TRACK));
		songFile.setTrackCount(parseIntTag(tag, FieldKey.TRACK_TOTAL));

		songFile.setName(tag.getFirst(FieldKey.TITLE));
		songFile.setAlbum(tag.getFirst(FieldKey.ALBUM));
		songFile.setArtist(tag.getFirst(FieldKey.ARTIST));
		songFile.setYear(Integer.valueOf(tag.getFirst(FieldKey.YEAR)));

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

	private Integer parseIntTag(Tag aTag, FieldKey aKey) {

		Integer result = null;

		try {
			result = Integer.valueOf(aTag.getFirst(aKey));
		} catch (NumberFormatException e) {}

		return result;
	}
}
