package net.dorokhov.pony.core.service;

import java.io.File;
import java.io.IOException;

/**
 * Checksum service calculates data checksum.
 *
 * It depends on implementation which checksum algorithm to use.
 */
public interface ChecksumService {

	/**
	 * Calculates file checksum.
	 *
	 * @param aFile file to calculate a checksum for
	 * @return checksum string
	 * @throws IOException in case something went wrong when reading the file
	 */
	public String calculateChecksum(File aFile) throws IOException;

	/**
	 * Calculates binary data checksum.
	 *
	 * @param aData binary data to calculate checksum for
	 * @return checksum string
	 */
	public String calculateChecksum(byte[] aData);

}
