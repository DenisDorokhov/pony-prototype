package net.dorokhov.pony.web.test;

import net.dorokhov.pony.core.domain.*;
import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.web.server.service.impl.DtoServiceImpl;
import net.dorokhov.pony.web.shared.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DtoServiceImplTest {

	private DtoServiceImpl service;

	@Before
	public void setUp() throws Exception {
		service = new DtoServiceImpl();
	}

	@Test
	public void testStatusToDto() throws Exception {

		LibraryScanner.Status status = new LibraryScanner.Status() {

			@Override
			public List<File> getTargetFiles() {

				List<File> targetFiles = new ArrayList<File>();

				targetFiles.add(new File("/target1"));
				targetFiles.add(new File("/target2"));

				return targetFiles;
			}

			@Override
			public String getDescription() {
				return "description";
			}

			@Override
			public double getProgress() {
				return 0.6;
			}

			@Override
			public int getStep() {
				return 2;
			}

			@Override
			public int getTotalSteps() {
				return 6;
			}
		};

		StatusDto dto = service.statusToDto(status);

		Assert.assertEquals(2, dto.getTargetFiles().size());
		Assert.assertEquals("/target1", dto.getTargetFiles().get(0));
		Assert.assertEquals("/target2", dto.getTargetFiles().get(1));

		Assert.assertEquals("description", dto.getDescription());
		Assert.assertEquals(0.6, dto.getProgress(), 0.01);
		Assert.assertEquals(2, dto.getStep());
		Assert.assertEquals(6, dto.getTotalSteps());
	}

	@Test
	public void testArtistToDto() throws Exception {

		ArtistDto dto = service.artistToDto(buildArtist());

		Assert.assertEquals(Long.valueOf(10), dto.getId());
		Assert.assertEquals(Long.valueOf(2), dto.getVersion());
		Assert.assertEquals("artist1", dto.getName());
		Assert.assertEquals(Long.valueOf(20), dto.getArtwork());
		Assert.assertNull(dto.getArtworkUrl());
	}

	@Test
	public void testAlbumToDto() throws Exception {

		AlbumDto dto = service.albumToDto(buildAlbum());

		checkAlbum(dto);
	}

	@Test
	public void testAlbumToSongsDto() throws Exception {

		Album album = buildAlbum();

		List<Song> songList = new ArrayList<Song>();

		for (int i = 0; i < 10; i++) {
			songList.add(buildSong(i, album));
		}

		AlbumSongsDto dto = service.albumToSongsDto(album, songList);

		checkAlbum(dto);

		Assert.assertEquals(10, dto.getSongs().size());

		for (int i = 0; i < 10; i++) {
			checkSong(i, dto.getSongs().get(i));
		}
	}

	@Test
	public void testSongToDto() throws Exception {

		SongDto dto = service.songToDto(buildSong(0, buildAlbum()));

		checkSong(0, dto);
	}

	private Artist buildArtist() {

		Artist artist = new Artist();

		artist.setId(10L);
		artist.setVersion(2L);
		artist.setName("artist1");

		StoredFile artwork = new StoredFile();

		artwork.setId(20L);

		artist.setArtwork(artwork);

		return artist;
	}

	private Album buildAlbum() {

		Album album = new Album();

		album.setId(30L);
		album.setVersion(3L);
		album.setName("album1");
		album.setYear(1986);

		StoredFile artwork = new StoredFile();

		artwork.setId(40L);

		album.setArtwork(artwork);
		album.setArtist(buildArtist());

		for (int i = 0; i < 10; i++) {
			album.getSongs().add(buildSong(i, album));
		}

		return album;
	}

	private Song buildSong(int aIndex, Album aAlbum) {

		Song song = new Song();

		song.setId((long)aIndex);
		song.setVersion(5L);
		song.setAlbum(aAlbum);

		SongFile songFile = new SongFile();

		songFile.setId((long)aIndex);
		songFile.setVersion(6L);
		songFile.setName("song" + aIndex);
		songFile.setArtist("artist1");
		songFile.setDuration(65);
		songFile.setDiscNumber(2);
		songFile.setTrackNumber(aIndex);

		song.setFile(songFile);

		return song;
	}

	private void checkAlbum(AlbumDto aDto) {
		Assert.assertEquals(Long.valueOf(30), aDto.getId());
		Assert.assertEquals(Long.valueOf(3), aDto.getVersion());
		Assert.assertEquals("album1", aDto.getName());
		Assert.assertEquals(Integer.valueOf(1986), aDto.getYear());
		Assert.assertEquals(Long.valueOf(40), aDto.getArtwork());
		Assert.assertEquals(Long.valueOf(10), aDto.getArtistId());
		Assert.assertEquals("artist1", aDto.getArtistName());
	}

	private void checkSong(int aIndex, SongDto aDto) {
		Assert.assertEquals(Long.valueOf(aIndex), aDto.getId());
		Assert.assertEquals(Long.valueOf(5), aDto.getVersion());
		Assert.assertNull(aDto.getFileUrl());
		Assert.assertEquals(Integer.valueOf(65), aDto.getDuration());
		Assert.assertEquals(Integer.valueOf(2), aDto.getDiscNumber());
		Assert.assertEquals(Integer.valueOf(aIndex), aDto.getTrackNumber());
		Assert.assertEquals("artist1", aDto.getArtist());
		Assert.assertEquals("song" + aIndex, aDto.getName());
		Assert.assertEquals(Long.valueOf(10), aDto.getArtistId());
		Assert.assertEquals("artist1", aDto.getArtistName());
		Assert.assertEquals(Long.valueOf(30), aDto.getAlbumId());
		Assert.assertEquals("album1", aDto.getAlbumName());
		Assert.assertEquals(Integer.valueOf(1986), aDto.getAlbumYear());
		Assert.assertEquals(Long.valueOf(40), aDto.getAlbumArtwork());
		Assert.assertNull(aDto.getAlbumArtworkUrl());
	}
}