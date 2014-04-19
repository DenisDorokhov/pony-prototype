package net.dorokhov.pony.core.service;

import java.io.File;

public interface ImageScalingService {

	public void scaleImage(byte[] aImage, String aFormat, File aOutFile) throws Exception;

	public void scaleImage(File aImage, String aFormat, File aOutFile) throws Exception;

}
