package net.dorokhov.pony.core.service.common;

import net.dorokhov.pony.core.service.ImageScalingService;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

@Service
public class ImageScalingServiceImpl implements ImageScalingService {

	@Override
	public void scaleImage(BufferedImage aImage, String aFormat, File aOutFile, int aWidth, int aHeight) throws Exception {

		BufferedImage image = Scalr.resize(aImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, aWidth, aHeight);

		ImageWriter writer = ImageIO.getImageWritersByFormatName(aFormat).next();

		writer.setOutput(new FileImageOutputStream(aOutFile));

		ImageWriteParam param = writer.getDefaultWriteParam();

		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(1.0f);

		writer.write(image);
	}

	@Override
	public void scaleImage(byte[] aImage, String aFormat, File aOutFile, int aWidth, int aHeight) throws Exception {

		BufferedImage image = ImageIO.read(new ByteArrayInputStream(aImage));

		scaleImage(image, aFormat, aOutFile, aWidth, aHeight);
	}

	@Override
	public void scaleImage(File aImage, String aFormat, File aOutFile, int aWidth, int aHeight) throws Exception {

		BufferedImage image = ImageIO.read(aImage);

		scaleImage(image, aFormat, aOutFile, aWidth, aHeight);
	}
}
