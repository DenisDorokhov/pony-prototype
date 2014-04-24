package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.domain.SongFile;
import net.dorokhov.pony.core.service.AlbumService;
import net.dorokhov.pony.core.service.ArtistService;
import net.dorokhov.pony.core.service.SongFileService;
import net.dorokhov.pony.core.service.SongService;
import net.dorokhov.pony.core.test.AbstractIntegrationCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolationException;
import java.util.List;

public class SongServiceIT extends AbstractIntegrationCase {

	private ArtistService artistService;

	private AlbumService albumService;

	private SongFileService songFileService;

	private SongService songService;

	@Before
	public void setUp() throws Exception {
		artistService = context.getBean(ArtistService.class);
		albumService = context.getBean(AlbumService.class);
		songFileService = context.getBean(SongFileService.class);
		songService = context.getBean(SongService.class);
	}

	@Test
	public void testCrud() {

		Artist artist = new Artist();

		artist.setName("artist");

		artistService.save(artist);

		Album album = new Album();

		album.setName("album");
		album.setArtist(artist);

		albumService.save(album);

		doTestSavingAndReading(album);

		Assert.assertEquals(2, songService.getCountByArtist(artist.getId()));
		Assert.assertEquals(2, songService.getCountByAlbum(album.getId()));

		Assert.assertEquals(2, songService.getByArtist(artist.getId()).size());

		List<Song> songList = songService.getByAlbum(album.getId());

		Assert.assertEquals(2, songList.size());

		songService.deleteByFileId(songList.get(0).getFile().getId());

		songList = songService.getByAlbum(album.getId());

		Assert.assertEquals(1, songList.size());

		songService.deleteById(songList.get(0).getId());

		Assert.assertEquals(0, songService.getByArtist(artist.getId()).size());
	}

	@Test
	public void testValidation() {

		Song song = new Song();

		song.setFile(null);
		song.setAlbum(null);

		boolean isExceptionThrown = false;

		try {
			songService.validate(song);
		} catch (ConstraintViolationException e) {

			isExceptionThrown = true;

			Assert.assertEquals(2, e.getConstraintViolations().size()); // null file, album
		}

		Assert.assertTrue(isExceptionThrown);
	}

	private void doTestSavingAndReading(Album aAlbum) {

		Song song = buildEntity(1, aAlbum);

		song = songService.save(song);

		checkEntity(song, 1, aAlbum);

		song = songService.getById(song.getId());

		checkEntity(song, 1, aAlbum);

		song = songService.getByFile(song.getFile().getId());

		checkEntity(song, 1, aAlbum);

		SongFile songFile = buildSongFile(3);

		songFile.setName("nameChanged");

		songFile = songFileService.save(songFile);

		song.setFile(songFile);

		song = songService.save(song);
		song = songService.getById(song.getId());

		Assert.assertEquals("nameChanged", song.getFile().getName());

		song = buildEntity(2, aAlbum);

		song = songService.save(song);

		checkEntity(song, 2, aAlbum);

		Assert.assertEquals(1, songService.search("name2").size());
	}

	private Song buildEntity(int aIndex, Album aAlbum) {

		SongFile songFile = buildSongFile(aIndex);

		songFile = songFileService.save(songFile);

		Song song = new Song();

		song.setFile(songFile);
		song.setAlbum(aAlbum);

		return song;
	}

	private void checkEntity(Song aSong, int aIndex, Album aAlbum) {
		Assert.assertEquals("path" + aIndex, aSong.getFile().getPath());
		Assert.assertEquals("name" + aIndex, aSong.getFile().getName());
		Assert.assertEquals(aAlbum.getId(), aSong.getAlbum().getId());
	}

	private SongFile buildSongFile(int aIndex) {

		SongFile songFile = new SongFile();

		songFile.setPath("path" + aIndex);
		songFile.setName("name" + aIndex);
		songFile.setFormat("type1");
		songFile.setMimeType("audio/mpeg");
		songFile.setSize(1000L);

		songFile.setDuration(100);
		songFile.setBitRate(2000L);

		return songFile;
	}

}
