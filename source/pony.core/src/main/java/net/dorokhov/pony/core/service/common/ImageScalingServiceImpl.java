package net.dorokhov.pony.core.service.common;

import net.coobird.thumbnailator.Thumbnails;
import net.dorokhov.pony.core.service.ImageScalingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Service
public class ImageScalingServiceImpl implements ImageScalingService {

	private static final double IMAGE_QUALITY = 1.0;

	private int imageWidth = 100;

	private int imageHeight = 100;

	@Value("${library.artworkSize}")
	public void setImageSize(String aArtworkSize) {

		String[] stringDimensions = aArtworkSize.split(",");

		if (stringDimensions.length == 2) {

			int[] dimensions = {0, 0};

			for (int i = 0; i < stringDimensions.length; i++) {
				dimensions[i] = Integer.valueOf(stringDimensions[i].trim());
			}

			imageWidth = dimensions[0];
			imageHeight = dimensions[1];

		} else {
			throw new RuntimeException("Incorrect artwork size value [" + aArtworkSize + "]");
		}
	}

	@Override
	public void scaleImage(byte[] aImage, String aFormat, File aOutFile) throws Exception {
		Thumbnails.of(new ByteArrayInputStream(aImage)).size(imageWidth, imageHeight).outputQuality(IMAGE_QUALITY).toOutputStream(new FileOutputStream(aOutFile));
	}

	@Override
	public void scaleImage(File aImage, String aFormat, File aOutFile) throws Exception {
		Thumbnails.of(new FileInputStream(aImage)).size(imageWidth, imageHeight).outputQuality(IMAGE_QUALITY).toOutputStream(new FileOutputStream(aOutFile));
	}
}
