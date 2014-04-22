package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.domain.SongFile;
import net.dorokhov.pony.core.service.SongFileService;
import net.dorokhov.pony.core.test.AbstractIntegrationCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.Assert.*;

public class SongFileServiceIT extends AbstractIntegrationCase {

	private SongFileService service;

	@Before
	public void setUp() throws Exception {
		service = context.getBean(SongFileService.class);
	}

	@Test
	public void testCrud() throws Exception {

		SongFile songFile = buildSongFile();

		songFile = service.save(songFile);

		checkSongFile(songFile);

		songFile = service.getById(songFile.getId());

		checkSongFile(songFile);

		songFile = service.getByPath("path1");

		checkSongFile(songFile);

		assertEquals(1L, service.getCount());

		List<SongFile> songFileList = service.getAll(new PageRequest(0, 100)).getContent();

		assertEquals(songFileList.size(), 1);

		songFile = songFileList.get(0);

		checkSongFile(songFile);

		service.deleteById(songFile.getId());

		songFile = service.getById(songFile.getId());

		assertNull(songFile);

		service.save(buildSongFile());
	}

	@Test
	public void testValidation() {

		SongFile songFile = new SongFile();

		songFile.setPath("");
		songFile.setFormat(" ");

		boolean isExceptionThrown = false;

		try {
			service.validate(songFile);
		} catch (ConstraintViolationException e) {

			isExceptionThrown = true;

			assertEquals(e.getConstraintViolations().size(), 6);
		}

		assertTrue(isExceptionThrown);
	}

	private SongFile buildSongFile() {

		SongFile songFile = new SongFile();

		songFile.setPath("path1");
		songFile.setFormat("type1");
		songFile.setMimeType("audio/mpeg");
		songFile.setSize(1000L);

		songFile.setDuration(100);
		songFile.setBitRate(2000L);

		songFile.setDiscNumber(1);
		songFile.setDiscCount(2);

		songFile.setTrackNumber(2);
		songFile.setTrackCount(8);

		songFile.setName("name1");
		songFile.setArtist("artist1");
		songFile.setAlbum("album1");
		songFile.setYear(1986);

		return songFile;
	}

	private void checkSongFile(SongFile aSongFile) {

		assertNotNull(aSongFile.getId());

		assertNotNull(aSongFile.getCreationDate());
		assertNotNull(aSongFile.getUpdateDate());

		assertEquals("path1", aSongFile.getPath());
		assertEquals("type1", aSongFile.getFormat());
		assertEquals(Long.valueOf(1000), aSongFile.getSize());

		assertEquals(Integer.valueOf(100), aSongFile.getDuration());
		assertEquals(Long.valueOf(2000), aSongFile.getBitRate());

		assertEquals(Integer.valueOf(1), aSongFile.getDiscNumber());
		assertEquals(Integer.valueOf(2), aSongFile.getDiscCount());

		assertEquals(Integer.valueOf(2), aSongFile.getTrackNumber());
		assertEquals(Integer.valueOf(8), aSongFile.getTrackCount());

		assertEquals("name1", aSongFile.getName());
		assertEquals("artist1", aSongFile.getArtist());
		assertEquals("album1", aSongFile.getAlbum());
		assertEquals(Integer.valueOf(1986), aSongFile.getYear());
	}

}
