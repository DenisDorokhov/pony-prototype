package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.domain.SongFile;
import net.dorokhov.pony.core.service.*;
import net.dorokhov.pony.core.test.AbstractIntegrationCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SearchServiceIT extends AbstractIntegrationCase {

	private ArtistService artistService;

	private AlbumService albumService;

	private SongFileService songFileService;

	private SongService songService;

	private SearchService searchService;

	@Before
	public void setUp() throws Exception {
		artistService = context.getBean(ArtistService.class);
		albumService = context.getBean(AlbumService.class);
		songFileService = context.getBean(SongFileService.class);
		songService = context.getBean(SongService.class);
		searchService = context.getBean(SearchService.class);
	}

	@Test
	public void test() {

		searchService.createIndex();

		Assert.assertEquals(0, searchService.searchArtists("art foo", 10).size());
		Assert.assertEquals(0, searchService.searchAlbums("alb foo", 10).size());
		Assert.assertEquals(0, searchService.searchSongs("so foo", 10).size());

		Artist artist = buildArtist();

		artist = artistService.save(artist);

		Album album = buildAlbum(artist);

		album = albumService.save(album);

		SongFile songFile = buildSongFile();

		songFile = songFileService.save(songFile);

		songService.save(buildSong(songFile, album));

		Assert.assertEquals(1, searchService.searchArtists("art foo", 10).size());
		Assert.assertEquals(1, searchService.searchAlbums("alb foo", 10).size());
		Assert.assertEquals(1, searchService.searchSongs("so foo", 10).size());

		Assert.assertEquals(0, searchService.searchArtists("artist2", 10).size());
		Assert.assertEquals(0, searchService.searchAlbums("album2", 10).size());
		Assert.assertEquals(0, searchService.searchSongs("song2", 10).size());

		searchService.clearIndex();

		Assert.assertEquals(0, searchService.searchArtists("art foo", 10).size());
		Assert.assertEquals(0, searchService.searchAlbums("alb foo", 10).size());
		Assert.assertEquals(0, searchService.searchSongs("so foo", 10).size());
	}

	private Artist buildArtist() {

		Artist artist = new Artist();

		artist.setName("artist1 foobar");

		return artist;
	}

	private Album buildAlbum(Artist aArtist) {

		Album album = new Album();

		album.setName("album1 foobar");
		album.setArtist(aArtist);

		return album;
	}

	private SongFile buildSongFile() {

		SongFile songFile = new SongFile();

		songFile.setPath("path");
		songFile.setFormat("type");
		songFile.setMimeType("audio/mpeg");
		songFile.setSize(1000L);

		songFile.setDuration(100);
		songFile.setBitRate(2000L);

		songFile.setName("song1 foobar");

		return songFile;
	}

	private Song buildSong(SongFile aSongFile, Album aAlbum) {

		Song song = new Song();

		song.setFile(aSongFile);
		song.setAlbum(aAlbum);

		return song;
	}

}
