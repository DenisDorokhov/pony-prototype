package net.dorokhov.pony.core.test.unit;

import net.dorokhov.pony.core.service.common.ArtworkServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ArtworkServiceImplTest {

	private static final String TEST_FILE_PATH = "data/image.png";
	private static final File TEST_FOLDER = new File(FileUtils.getTempDirectory(), "ArtworkServiceImplTest");

	private ArtworkServiceImpl service;

	@Before
	public void setUp() throws Exception {

		service = new ArtworkServiceImpl();

		FileUtils.deleteDirectory(TEST_FOLDER);
	}

	@After
	public void tearDown() throws Exception {
		FileUtils.deleteDirectory(TEST_FOLDER);
	}

	@Test
	public void testDiscovery() throws Exception {

		Set<String> artworkFileNames = new HashSet<String>();
		Set<String> artworkFileExtensions = new HashSet<String>();
		Set<String> artworkFolderNames = new HashSet<String>();

		CollectionUtils.addAll(artworkFileNames, new String[]{"cover", "folder"});
		CollectionUtils.addAll(artworkFileExtensions, new String[]{"jpg", "png"});
		CollectionUtils.addAll(artworkFolderNames, new String[]{"artwork"});

		service.setArtworkFileNames(artworkFileNames);
		service.setArtworkFileExtensions(artworkFileExtensions);
		service.setArtworkFolderNames(artworkFolderNames);

		File imageFile = new ClassPathResource(TEST_FILE_PATH).getFile();

		TEST_FOLDER.mkdir();

		File artwork;

		FileUtils.touch(new File(TEST_FOLDER, "test.tmp"));

		artwork = service.discoverArtwork(TEST_FOLDER); // TEST_FOLDER: {test.tmp}

		Assert.assertNull(artwork);

		File artworkFolder;

		artworkFolder = new File(TEST_FOLDER, "art");

		FileUtils.copyFile(imageFile, new File(artworkFolder, "folder.jpg"));

		artworkFolder = new File(TEST_FOLDER, "artwork");

		FileUtils.copyFile(imageFile, new File(artworkFolder, "cover.jpg"));

		artwork = service.discoverArtwork(TEST_FOLDER); // TEST_FOLDER: {test.tmp, art: {cover.jpg}, artwork: {cover.jpg}}

		Assert.assertEquals("cover.jpg", artwork.getName());

		FileUtils.copyFile(imageFile, new File(TEST_FOLDER, "foobar.png"));

		artwork = service.discoverArtwork(TEST_FOLDER); // TEST_FOLDER: {test.tmp, foobar.png, art: {cover.jpg}, artwork: {cover.jpg}}

		Assert.assertEquals("foobar.png", artwork.getName());

		FileUtils.copyFile(imageFile, new File(TEST_FOLDER, "folder.png"));

		artwork = service.discoverArtwork(TEST_FOLDER); // TEST_FOLDER: {test.tmp, foobar.png, folder.png, art: {cover.jpg}, artwork: {cover.jpg}}

		Assert.assertEquals("folder.png", artwork.getName());
	}

	@Test
	public void testConfiguration() {

		Set<String> result;

		service.setArtworkFileNames(",cover, folder,artwork");

		result = service.getArtworkFileNames();

		Assert.assertEquals(3, result.size());
		Assert.assertTrue(result.contains("cover"));
		Assert.assertTrue(result.contains("folder"));
		Assert.assertTrue(result.contains("artwork"));

		service.setArtworkFileExtensions("jpg,png, jpeg,");

		result = service.getArtworkFileExtensions();

		Assert.assertEquals(3, result.size());
		Assert.assertTrue(result.contains("jpg"));
		Assert.assertTrue(result.contains("png"));
		Assert.assertTrue(result.contains("jpeg"));

		service.setArtworkFolderNames("artwork, covers");

		result = service.getArtworkFolderNames();

		Assert.assertEquals(2, result.size());
		Assert.assertTrue(result.contains("artwork"));
		Assert.assertTrue(result.contains("covers"));
	}

}
