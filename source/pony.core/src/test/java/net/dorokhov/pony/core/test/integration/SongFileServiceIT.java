package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.domain.SongFile;
import net.dorokhov.pony.core.domain.StoredFile;
import net.dorokhov.pony.core.service.SongFileService;
import net.dorokhov.pony.core.service.StoredFileService;
import net.dorokhov.pony.core.test.AbstractIntegrationCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.validation.ConstraintViolationException;

public class SongFileServiceIT extends AbstractIntegrationCase {

	private static final String TEST_ARTWORK_PATH = "data/image.png";
	private static final String TEST_ARTWORK_MIME_TYPE = "image/png";

	private SongFileService songFileService;

	private StoredFileService storedFileService;

	@Before
	public void setUp() throws Exception {
		songFileService = context.getBean(SongFileService.class);
		storedFileService = context.getBean(StoredFileService.class);
	}

	@Test
	public void testCrud() {

		SongFile songFile = buildSongFile(1);

		songFile = songFileService.save(songFile);

		checkSongFile(songFile, 1);

		songFile = songFileService.getById(songFile.getId());

		checkSongFile(songFile, 1);

		songFile = songFileService.getByPath("path1");

		checkSongFile(songFile, 1);

		songFile.setYear(1999);

		songFile = songFileService.save(songFile);
		songFile = songFileService.getById(songFile.getId());

		Assert.assertEquals(Integer.valueOf(1999), songFile.getYear());

		songFile = buildSongFile(2);

		songFile = songFileService.save(songFile);

		checkSongFile(songFile, 2);

		Assert.assertEquals(2, songFileService.getCount());

		Page<SongFile> songFilePage = songFileService.getAll(new PageRequest(0, 100));

		Assert.assertEquals(songFilePage.getTotalElements(), 2);

		songFile = songFilePage.getContent().get(0);

		songFileService.deleteById(songFile.getId());

		Assert.assertNull(songFileService.getById(songFile.getId()));
	}

	@Test
	public void testArtwork() throws Exception {

		StoredFileService.SaveCommand artworkCommand = new StoredFileService.SaveCommand(StoredFileService.SaveCommand.Type.COPY, new ClassPathResource(TEST_ARTWORK_PATH).getFile());

		artworkCommand.setName("artwork");
		artworkCommand.setMimeType(TEST_ARTWORK_MIME_TYPE);
		artworkCommand.setChecksum("someChecksum");

		StoredFile artwork = storedFileService.save(artworkCommand);

		SongFile songFile = buildSongFile(1);

		songFile.setArtwork(artwork);

		songFileService.save(songFile);

		Assert.assertEquals(1, songFileService.getCountByArtwork(artwork.getId()));
	}

	@Test
	public void testValidation() {

		SongFile songFile = new SongFile();

		songFile.setPath("");
		songFile.setFormat(" ");

		boolean isExceptionThrown = false;

		try {
			songFileService.validate(songFile);
		} catch (ConstraintViolationException e) {

			isExceptionThrown = true;

			Assert.assertEquals(e.getConstraintViolations().size(), 6); // null path, format, mimeType, size, duration, bitRate
		}

		Assert.assertTrue(isExceptionThrown);
	}

	private SongFile buildSongFile(int aIndex) {

		SongFile songFile = new SongFile();

		songFile.setPath("path" + aIndex);
		songFile.setFormat("type1");
		songFile.setMimeType("audio/mpeg");
		songFile.setSize(1000L);

		songFile.setDuration(100);
		songFile.setBitRate(2000L);

		songFile.setDiscNumber(1);
		songFile.setDiscCount(2);

		songFile.setTrackNumber(2);
		songFile.setTrackCount(8);

		songFile.setName("name" + aIndex);
		songFile.setArtist("artist" + aIndex);
		songFile.setAlbum("album" + aIndex);
		songFile.setYear(1986);

		return songFile;
	}

	private void checkSongFile(SongFile aEntity, int aIndex) {

		Assert.assertNotNull(aEntity.getId());
		Assert.assertNotNull(aEntity.getVersion());

		Assert.assertNotNull(aEntity.getCreationDate());
		Assert.assertNotNull(aEntity.getUpdateDate());

		Assert.assertEquals("path" + aIndex, aEntity.getPath());
		Assert.assertEquals("type1", aEntity.getFormat());
		Assert.assertEquals(Long.valueOf(1000), aEntity.getSize());

		Assert.assertEquals(Integer.valueOf(100), aEntity.getDuration());
		Assert.assertEquals(Long.valueOf(2000), aEntity.getBitRate());

		Assert.assertEquals(Integer.valueOf(1), aEntity.getDiscNumber());
		Assert.assertEquals(Integer.valueOf(2), aEntity.getDiscCount());

		Assert.assertEquals(Integer.valueOf(2), aEntity.getTrackNumber());
		Assert.assertEquals(Integer.valueOf(8), aEntity.getTrackCount());

		Assert.assertEquals("name" + aIndex, aEntity.getName());
		Assert.assertEquals("artist" + aIndex, aEntity.getArtist());
		Assert.assertEquals("album" + aIndex, aEntity.getAlbum());
		Assert.assertEquals(Integer.valueOf(1986), aEntity.getYear());
	}

}
