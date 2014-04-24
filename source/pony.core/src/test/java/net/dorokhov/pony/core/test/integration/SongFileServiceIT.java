package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.domain.SongFile;
import net.dorokhov.pony.core.domain.StorageTask;
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

		doTestSavingAndReading();

		Assert.assertEquals(2, songFileService.getCount());

		Page<SongFile> songFilePage = songFileService.getAll(new PageRequest(0, 100));

		Assert.assertEquals(songFilePage.getTotalElements(), 2);

		SongFile songFile = songFilePage.getContent().get(0);

		songFileService.deleteById(songFile.getId());

		Assert.assertNull(songFileService.getById(songFile.getId()));
	}

	@Test
	public void testArtwork() throws Exception {

		StorageTask artworkTask = new StorageTask(StorageTask.Type.COPY, new ClassPathResource(TEST_ARTWORK_PATH).getFile());

		artworkTask.setName("artwork");
		artworkTask.setMimeType(TEST_ARTWORK_MIME_TYPE);
		artworkTask.setChecksum("someChecksum");

		StoredFile artwork = storedFileService.save(artworkTask);

		SongFile songFile = buildEntity(1);

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

	private void doTestSavingAndReading() {

		SongFile songFile = buildEntity(1);

		songFile = songFileService.save(songFile);

		checkEntity(songFile, 1);

		songFile = songFileService.getById(songFile.getId());

		checkEntity(songFile, 1);

		songFile = songFileService.getByPath("path1");

		checkEntity(songFile, 1);

		songFile.setYear(1999);

		songFile = songFileService.save(songFile);
		songFile = songFileService.getById(songFile.getId());

		Assert.assertEquals(Integer.valueOf(1999), songFile.getYear());

		songFile = buildEntity(2);

		songFile = songFileService.save(songFile);

		checkEntity(songFile, 2);
	}

	private SongFile buildEntity(int aIndex) {

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

	private void checkEntity(SongFile aSongFile, int aIndex) {

		Assert.assertNotNull(aSongFile.getId());

		Assert.assertNotNull(aSongFile.getCreationDate());
		Assert.assertNotNull(aSongFile.getUpdateDate());

		Assert.assertEquals("path" + aIndex, aSongFile.getPath());
		Assert.assertEquals("type1", aSongFile.getFormat());
		Assert.assertEquals(Long.valueOf(1000), aSongFile.getSize());

		Assert.assertEquals(Integer.valueOf(100), aSongFile.getDuration());
		Assert.assertEquals(Long.valueOf(2000), aSongFile.getBitRate());

		Assert.assertEquals(Integer.valueOf(1), aSongFile.getDiscNumber());
		Assert.assertEquals(Integer.valueOf(2), aSongFile.getDiscCount());

		Assert.assertEquals(Integer.valueOf(2), aSongFile.getTrackNumber());
		Assert.assertEquals(Integer.valueOf(8), aSongFile.getTrackCount());

		Assert.assertEquals("name" + aIndex, aSongFile.getName());
		Assert.assertEquals("artist" + aIndex, aSongFile.getArtist());
		Assert.assertEquals("album" + aIndex, aSongFile.getAlbum());
		Assert.assertEquals(Integer.valueOf(1986), aSongFile.getYear());
	}

}
