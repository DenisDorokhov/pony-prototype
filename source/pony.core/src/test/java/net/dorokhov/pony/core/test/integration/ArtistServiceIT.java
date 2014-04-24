package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.domain.StorageTask;
import net.dorokhov.pony.core.domain.StoredFile;
import net.dorokhov.pony.core.service.ArtistService;
import net.dorokhov.pony.core.service.StoredFileService;
import net.dorokhov.pony.core.test.AbstractIntegrationCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import javax.validation.ConstraintViolationException;

public class ArtistServiceIT extends AbstractIntegrationCase {

	private static final String TEST_ARTWORK_PATH = "data/image.png";
	private static final String TEST_ARTWORK_MIME_TYPE = "image/png";

	private ArtistService artistService;

	private StoredFileService storedFileService;

	@Before
	public void setUp() throws Exception {
		artistService = context.getBean(ArtistService.class);
		storedFileService = context.getBean(StoredFileService.class);
	}

	@Test
	public void testCrud() {

		doTestSavingReading();

		Assert.assertEquals(2, artistService.getCount());

		artistService.deleteById(artistService.getAll().get(0).getId());

		Assert.assertEquals(1, artistService.getAll().size());
	}

	@Test
	public void testArtwork() throws Exception {

		StorageTask artworkTask = new StorageTask(StorageTask.Type.COPY, new ClassPathResource(TEST_ARTWORK_PATH).getFile());

		artworkTask.setName("artwork");
		artworkTask.setMimeType(TEST_ARTWORK_MIME_TYPE);
		artworkTask.setChecksum("someChecksum");

		StoredFile artwork = storedFileService.save(artworkTask);

		Artist artist = buildEntity(1);

		artist.setArtwork(artwork);

		artist = artistService.save(artist);

		Assert.assertEquals(1, artistService.getCountByArtwork(artwork.getId()));
		Assert.assertEquals(artist.getId(), artistService.getByArtwork(artist.getId()).get(0).getId());
	}

	@Test
	public void testValidation() {

		Artist artist = new Artist();

		artist.setName(null);

		boolean isExceptionThrown = false;

		try {
			artistService.validate(artist);
		} catch (ConstraintViolationException e) {

			isExceptionThrown = true;

			Assert.assertEquals(1, e.getConstraintViolations().size());
		}

		Assert.assertTrue(isExceptionThrown);
	}

	private void doTestSavingReading() {

		Artist artist = buildEntity(1);

		artist = artistService.save(artist);

		checkEntity(artist, 1);

		artist = artistService.getById(artist.getId());

		checkEntity(artist, 1);

		artist = artistService.getByName("name1");

		checkEntity(artist, 1);

		artist.setName("nameChanged");

		artist = artistService.save(artist);
		artist = artistService.getById(artist.getId());

		Assert.assertEquals("nameChanged", artist.getName());

		artist = buildEntity(2);

		artist = artistService.save(artist);

		checkEntity(artist, 2);

		Assert.assertEquals(1, artistService.search("name2").size());
	}

	private Artist buildEntity(int aIndex) {

		Artist artist = new Artist();

		artist.setName("name" + aIndex);

		return artist;
	}

	private void checkEntity(Artist aArtist, int aIndex) {
		Assert.assertEquals("name" + aIndex, aArtist.getName());
	}

}
