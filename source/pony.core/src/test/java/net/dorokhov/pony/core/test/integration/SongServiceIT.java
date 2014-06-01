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

		Song song = buildSong(1, album);

		song = songService.save(song);

		checkSong(song, 1, album);

		song = songService.getById(song.getId());

		checkSong(song, 1, album);

		song = songService.getByFile(song.getFile().getId());

		checkSong(song, 1, album);

		SongFile songFile = buildSongFile(3);

		songFile.setName("nameChanged");

		songFile = songFileService.save(songFile);

		song.setFile(songFile);

		song = songService.save(song);
		song = songService.getById(song.getId());

		Assert.assertEquals("nameChanged", song.getFile().getName());

		song = buildSong(2, album);

		song = songService.save(song);

		checkSong(song, 2, album);

		Assert.assertEquals(1, songService.search("name2").size());

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

	private Song buildSong(int aIndex, Album aAlbum) {

		SongFile songFile = buildSongFile(aIndex);

		songFile = songFileService.save(songFile);

		Song song = new Song();

		song.setFile(songFile);
		song.setAlbum(aAlbum);

		return song;
	}

	private void checkSong(Song aEntity, int aIndex, Album aAlbum) {

		Assert.assertNotNull(aEntity.getId());
		Assert.assertNotNull(aEntity.getCreationDate());
		Assert.assertNotNull(aEntity.getUpdateDate());

		Assert.assertEquals("path" + aIndex, aEntity.getFile().getPath());
		Assert.assertEquals("name" + aIndex, aEntity.getFile().getName());

		Assert.assertEquals(aAlbum.getId(), aEntity.getAlbum().getId());
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
