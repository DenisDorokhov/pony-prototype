package net.dorokhov.pony.core.service;

import java.awt.image.BufferedImage;
import java.io.File;

public interface ImageScalingService {

	public void scaleImage(BufferedImage aImage, String aFormat, File aOutFile, int aWidth, int aHeight) throws Exception;

	public void scaleImage(byte[] aImage, String aFormat, File aOutFile, int aWidth, int aHeight) throws Exception;

	public void scaleImage(File aImage, String aFormat, File aOutFile, int aWidth, int aHeight) throws Exception;

}
