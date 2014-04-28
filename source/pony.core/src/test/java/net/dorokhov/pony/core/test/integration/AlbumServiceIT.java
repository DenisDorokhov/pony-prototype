package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.domain.Artist;
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

		Album album = buildAlbum(1, artist);

		album = albumService.save(album);

		checkAlbum(album, 1, artist);

		album = albumService.getById(album.getId());

		checkAlbum(album, 1, artist);

		album = albumService.getByArtistAndName(artist.getId(), "name1");

		checkAlbum(album, 1, artist);

		album.setName("nameChanged");

		album = albumService.save(album);
		album = albumService.getById(album.getId());

		Assert.assertEquals("nameChanged", album.getName());

		album = buildAlbum(2, artist);

		album = albumService.save(album);

		checkAlbum(album, 2, artist);

		Assert.assertEquals(1, albumService.search("name2").size());

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

		StoredFileService.SaveCommand artworkCommand = new StoredFileService.SaveCommand(StoredFileService.SaveCommand.Type.COPY, new ClassPathResource(TEST_ARTWORK_PATH).getFile());

		artworkCommand.setName("artwork");
		artworkCommand.setMimeType(TEST_ARTWORK_MIME_TYPE);
		artworkCommand.setChecksum("someChecksum");

		StoredFile artwork = storedFileService.save(artworkCommand);

		Album album = buildAlbum(1, artist);

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

	private Album buildAlbum(int aIndex, Artist aArtist) {

		Album album = new Album();

		album.setName("name" + aIndex);
		album.setYear(1986);
		album.setArtist(aArtist);

		return album;
	}

	private void checkAlbum(Album aAlbum, int aIndex, Artist aArtist) {

		Assert.assertNotNull(aAlbum.getId());
		Assert.assertNotNull(aAlbum.getVersion());

		Assert.assertNotNull(aAlbum.getCreationDate());
		Assert.assertNotNull(aAlbum.getUpdateDate());

		Assert.assertEquals("name" + aIndex, aAlbum.getName());
		Assert.assertEquals(Integer.valueOf(1986), aAlbum.getYear());

		Assert.assertEquals(aArtist.getId(), aAlbum.getArtist().getId());
	}

}
