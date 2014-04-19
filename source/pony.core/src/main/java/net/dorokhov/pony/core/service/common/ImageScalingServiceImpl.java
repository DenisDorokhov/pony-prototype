package net.dorokhov.pony.core.service.common;

import net.coobird.thumbnailator.Thumbnails;
import net.dorokhov.pony.core.service.ImageScalingService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Service
public class ImageScalingServiceImpl implements ImageScalingService {

	private static final double IMAGE_QUALITY = 1.0;

	@Override
	public void scaleImage(byte[] aImage, String aFormat, File aOutFile, int aWidth, int aHeight) throws Exception {
		Thumbnails.of(new ByteArrayInputStream(aImage)).size(aWidth, aHeight).outputQuality(IMAGE_QUALITY).toOutputStream(new FileOutputStream(aOutFile));
	}

	@Override
	public void scaleImage(File aImage, String aFormat, File aOutFile, int aWidth, int aHeight) throws Exception {
		Thumbnails.of(new FileInputStream(aImage)).size(aWidth, aHeight).outputQuality(IMAGE_QUALITY).toOutputStream(new FileOutputStream(aOutFile));
	}
}
