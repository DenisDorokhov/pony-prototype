package net.dorokhov.pony.core.service;

import java.io.File;

/**
 * Image scaling service is used to create thumbnails.
 */
public interface ImageScalingService {

	/**
	 * Scales image to allowed size. Output file will have the same format as the original one.
	 *
	 * @param aImage image as byte array
	 * @param aOutFile output file
	 * @throws Exception in case something went wrong
	 */
	public void scaleImage(byte[] aImage, File aOutFile) throws Exception;

	/**
	 * Scales image to allowed size. Output file will have the same format as the original one.
	 *
	 * @param aImage image file
	 * @param aOutFile output file
	 * @throws Exception in case something went wrong
	 */
	public void scaleImage(File aImage, File aOutFile) throws Exception;

}
