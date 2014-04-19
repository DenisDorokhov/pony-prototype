package net.dorokhov.pony.core.service;

import java.io.File;

public interface ImageScalingService {

	public void scaleImage(byte[] aImage, String aFormat, File aOutFile, int aWidth, int aHeight) throws Exception;

	public void scaleImage(File aImage, String aFormat, File aOutFile, int aWidth, int aHeight) throws Exception;

}
