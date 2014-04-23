package net.dorokhov.pony.core.service;

import java.io.File;

/**
 * Thumbnail service is used to create thumbnails.
 */
public interface ThumbnailService {

	/**
	 * Makes a thumbnail from image data. Output file will have the same format as the original image.
	 *
	 * @param aImage image data
	 * @param aOutFile output file
	 * @throws Exception in case something went wrong
	 */
	public void makeThumbnail(byte[] aImage, File aOutFile) throws Exception;

	/**
	 * Makes a thumbnail from image file. Output file will have the same format as the original image.
	 *
	 * @param aImage image file
	 * @param aOutFile output file
	 * @throws Exception in case something went wrong
	 */
	public void makeThumbnail(File aImage, File aOutFile) throws Exception;

}
