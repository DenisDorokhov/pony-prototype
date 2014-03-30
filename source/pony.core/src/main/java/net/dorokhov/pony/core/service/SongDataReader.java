package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.SongData;

import java.io.File;

/**
 * Song data reader.
 */
public interface SongDataReader {

	/**
	 * Reads song data from file system.
	 *
	 * @param aFile file to read song data from
	 * @return song file data
	 * @throws Exception in case something went wrong
	 */
	public SongData readSongData(File aFile) throws Exception;

}
