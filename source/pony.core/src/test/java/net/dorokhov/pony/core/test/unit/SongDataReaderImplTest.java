package net.dorokhov.pony.core.test.unit;

import net.dorokhov.pony.core.domain.SongData;
import net.dorokhov.pony.core.service.common.ChecksumServiceImpl;
import net.dorokhov.pony.core.service.common.SongDataReaderImpl;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

public class SongDataReaderImplTest {

	private static final String TEST_FILE_PATH = "data/Metallica-Battery-with_artwork.mp3"; // see tags in data/mp3-info.txt
	private static final File TEST_FILE = new File(FileUtils.getTempDirectory(), "TestSongDataReaderImpl.mp3");

	private SongDataReaderImpl service;

	@Before
	public void setUp() throws Exception {

		service = new SongDataReaderImpl();
		service.setChecksumService(new ChecksumServiceImpl());

		FileUtils.copyFile(new ClassPathResource(TEST_FILE_PATH).getFile(), TEST_FILE);
	}

	@After
	public void tearDown() {
		TEST_FILE.delete();
	}

	@Test
	public void test() throws Exception {

		SongData songData = service.readSongData(TEST_FILE);

		Assert.assertEquals(TEST_FILE.getAbsolutePath(), songData.getPath());
		Assert.assertEquals("MPEG-1 Layer 3", songData.getFormat());
		Assert.assertEquals("audio/mpeg", songData.getMimeType());
		Assert.assertEquals(Long.valueOf(24797), songData.getSize());
		Assert.assertEquals(Integer.valueOf(1), songData.getDuration());
		Assert.assertEquals(Long.valueOf(128), songData.getBitRate());
		Assert.assertEquals(Integer.valueOf(1), songData.getDiscNumber());
		Assert.assertEquals(Integer.valueOf(1), songData.getDiscCount());
		Assert.assertEquals(Integer.valueOf(1), songData.getTrackNumber());
		Assert.assertEquals(Integer.valueOf(8), songData.getTrackCount());
		Assert.assertEquals("Battery", songData.getName());
		Assert.assertEquals("Metallica", songData.getArtist());
		Assert.assertEquals("Metallica", songData.getAlbumArtist());
		Assert.assertEquals("Master Of Puppets", songData.getAlbum());
		Assert.assertEquals(Integer.valueOf(1986), songData.getYear());
		Assert.assertEquals("Rock", songData.getGenre());
		Assert.assertEquals("image/jpeg", songData.getArtwork().getMimeType());
		Assert.assertEquals("0a6632570700e5f595a75999508fc46d", songData.getArtwork().getChecksum());
	}

}
