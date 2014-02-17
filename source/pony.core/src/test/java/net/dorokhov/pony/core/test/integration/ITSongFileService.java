package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.entity.SongFile;
import net.dorokhov.pony.core.service.SongFileService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.Assert.*;

public class ITSongFileService extends AbstractIntegrationCase {

	private SongFileService service;

	@Before
	public void setUp() throws Exception {
		service = context.getBean(SongFileService.class);
	}

	@Test
	public void testCrud() {

		SongFile songFile = new SongFile();

		songFile.setPath("path1");
		songFile.setType("type1");

		songFile.setSize(1000L);
		songFile.setDuration(100L);

		songFile.setDiscNumber(1);
		songFile.setTrackNumber(2);

		songFile.setName("name1");
		songFile.setArtist("artist1");
		songFile.setAlbum("album1");
		songFile.setYear(1986);

		songFile = service.save(songFile);

		checkProperties(songFile);

		songFile = service.getById(songFile.getId());

		checkProperties(songFile);

		assertEquals((long)service.getCount(), 1L);

		List<SongFile> songFileList = service.getAll(new PageRequest(0, 100)).getContent();

		assertEquals(songFileList.size(), 1);

		songFile = songFileList.get(0);

		checkProperties(songFile);

		service.deleteById(songFile.getId());

		songFile = service.getById(songFile.getId());

		assertNull(songFile);
	}

	@Test
	public void testValidation() {

		SongFile songFile = new SongFile();

		songFile.setPath("");
		songFile.setType(" ");

		boolean isExceptionThrown = false;

		try {
			service.validate(songFile);
		} catch (ConstraintViolationException e) {

			isExceptionThrown = true;

			assertEquals(e.getConstraintViolations().size(), 2);
		}

		assertTrue(isExceptionThrown);
	}

	private void checkProperties(SongFile aSongFile) {

		assertNotNull(aSongFile.getId());

		assertNotNull(aSongFile.getCreationDate());
		assertNotNull(aSongFile.getUpdateDate());

		assertEquals(aSongFile.getPath(), "path1");
		assertEquals(aSongFile.getType(), "type1");

		assertEquals((long)aSongFile.getSize(), 1000L);
		assertEquals((long)aSongFile.getDuration(), 100L);

		assertEquals((int)aSongFile.getDiscNumber(), 1);
		assertEquals((int)aSongFile.getTrackNumber(), 2);

		assertEquals(aSongFile.getName(), "name1");
		assertEquals(aSongFile.getArtist(), "artist1");
		assertEquals(aSongFile.getAlbum(), "album1");
		assertEquals(aSongFile.getYear(), 1986);
	}

}
