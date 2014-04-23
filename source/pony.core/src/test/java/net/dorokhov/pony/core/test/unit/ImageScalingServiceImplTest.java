package net.dorokhov.pony.core.test.unit;

import net.dorokhov.pony.core.service.common.ImageScalingServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageScalingServiceImplTest {

	private static final String TEST_FILE_PATH = "data/image.png"; // red picture 100x100
	private static final File TEST_TARGET_FILE = new File(FileUtils.getTempDirectory(), "ImageScalingServiceImplTest.jpg");

	private ImageScalingServiceImpl service;

	@Before
	public void setUp() {

		service = new ImageScalingServiceImpl();
		service.setImageSize("50,50");

		TEST_TARGET_FILE.delete();
	}

	@After
	public void tearDown() {
		TEST_TARGET_FILE.delete();
	}

	@Test
	public void test() throws Exception {

		File sourceFile = new ClassPathResource(TEST_FILE_PATH).getFile();

		service.scaleImage(sourceFile, TEST_TARGET_FILE);

		checkTargetImageSize();

		service.scaleImage(FileUtils.readFileToByteArray(sourceFile), TEST_TARGET_FILE);

		checkTargetImageSize();
	}

	private void checkTargetImageSize() throws Exception{

		BufferedImage targetImage = ImageIO.read(TEST_TARGET_FILE);

		Assert.assertTrue(targetImage.getWidth() <= service.getImageWidth());
		Assert.assertTrue(targetImage.getHeight() <= service.getImageHeight());
	}

}
