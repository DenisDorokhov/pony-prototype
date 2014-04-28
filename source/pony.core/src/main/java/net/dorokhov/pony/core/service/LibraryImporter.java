package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.SongFile;

import java.io.File;

/**
 * Library service.
 *
 * Imports song files into library database.
 */
public interface LibraryImporter {

	/**
	 * Imports song files creating new song entities (artists, albums and songs) and updating existing. This method is
	 * supposed to be called from different threads.
	 *
	 * @param aFile song file to import
	 * @return imported song file
	 */
	public SongFile importSong(File aFile);

}
