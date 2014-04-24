package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.domain.StorageTask;
import net.dorokhov.pony.core.domain.StoredFile;
import net.dorokhov.pony.core.service.AlbumService;
import net.dorokhov.pony.core.service.ArtistService;
import net.dorokhov.pony.core.service.StoredFileService;
import net.dorokhov.pony.core.test.AbstractIntegrationCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;

import javax.validation.ConstraintViolationException;

public class AlbumServiceIT extends AbstractIntegrationCase {

	private static final String TEST_ARTWORK_PATH = "data/image.png";
	private static final String TEST_ARTWORK_MIME_TYPE = "image/png";

	private ArtistService artistService;

	private AlbumService albumService;

	private StoredFileService storedFileService;

	@Before
	public void setUp() throws Exception {
		artistService = context.getBean(ArtistService.class);
		albumService = context.getBean(AlbumService.class);
		storedFileService = context.getBean(StoredFileService.class);
	}

	@Test
	public void testCrud() {

		Artist artist = new Artist();

		artist.setName("artist");

		artistService.save(artist);

		doTestSavingAndReading(artist);

		Assert.assertEquals(2, albumService.getCountByArtist(artist.getId()));
		Assert.assertEquals(2, albumService.getAll(new PageRequest(0, 100)).getTotalElements());

		albumService.deleteById(albumService.getByArtist(artist.getId()).get(0).getId());

		Assert.assertEquals(1, albumService.getByArtist(artist.getId()).size());
	}

	@Test
	public void testArtwork() throws Exception {

		Artist artist = new Artist();

		artist.setName("artist");

		artistService.save(artist);

		StorageTask artworkTask = new StorageTask(StorageTask.Type.COPY, new ClassPathResource(TEST_ARTWORK_PATH).getFile());

		artworkTask.setName("artwork");
		artworkTask.setMimeType(TEST_ARTWORK_MIME_TYPE);
		artworkTask.setChecksum("someChecksum");

		StoredFile artwork = storedFileService.save(artworkTask);

		Album album = buildEntity(1, artist);

		album.setArtwork(artwork);

		album = albumService.save(album);

		Assert.assertEquals(1, albumService.getCountByArtwork(artwork.getId()));
		Assert.assertEquals(album.getId(), albumService.getByArtwork(album.getId()).get(0).getId());
	}

	@Test
	public void testValidation() {

		Album album = new Album();

		album.setName(null);
		album.setArtist(null);

		boolean isExceptionThrown = false;

		try {
			albumService.validate(album);
		} catch (ConstraintViolationException e) {

			isExceptionThrown = true;

			Assert.assertEquals(2, e.getConstraintViolations().size()); // null name, artist
		}

		Assert.assertTrue(isExceptionThrown);
	}

	private void doTestSavingAndReading(Artist aArtist) {

		Album album = buildEntity(1, aArtist);

		album = albumService.save(album);

		checkEntity(album, 1, aArtist);

		album = albumService.getById(album.getId());

		checkEntity(album, 1, aArtist);

		album = albumService.getByArtistAndName(aArtist.getId(), "name1");

		checkEntity(album, 1, aArtist);

		album.setName("nameChanged");

		album = albumService.save(album);
		album = albumService.getById(album.getId());

		Assert.assertEquals("nameChanged", album.getName());

		album = buildEntity(2, aArtist);

		album = albumService.save(album);

		checkEntity(album, 2, aArtist);

		Assert.assertEquals(1, albumService.search("name2").size());
	}

	private Album buildEntity(int aIndex, Artist aArtist) {

		Album album = new Album();

		album.setName("name" + aIndex);
		album.setYear(1986);
		album.setArtist(aArtist);

		return album;
	}

	private void checkEntity(Album aAlbum, int aIndex, Artist aArtist) {
		Assert.assertEquals("name" + aIndex, aAlbum.getName());
		Assert.assertEquals(Integer.valueOf(1986), aAlbum.getYear());
		Assert.assertEquals(aArtist.getId(), aAlbum.getArtist().getId());
	}

}
