package net.dorokhov.pony.core.service;

import java.io.File;

/**
 * Mime type service.
 *
 * Utility to perform mime type based operations.
 */
public interface MimeTypeService {

	/**
	 * Discovers file mime type.
	 *
	 * @param aFile file to discover mime type for
	 * @return file mime type or null if mime type can't be discovered
	 */
	public String getFileMimeType(File aFile);

	/**
	 * Discovers file extension by mime type.
	 *
	 * @param aMimeType mime type
	 * @return file extension or null if extension can't be discovered
	 */
	public String getFileExtension(String aMimeType);

}
