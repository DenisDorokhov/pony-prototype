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
	 * @return song import result
	 */
	public Result importSong(File aFile);

	/**
	 * Song import result.
	 */
	public interface Result {

		/**
		 * Gets imported file.
		 *
		 * @return imported song file
		 */
		public SongFile getFile();

		/**
		 * Gets imported file modification status.
		 *
		 * @return true if SongFile has been modified, false otherwise
		 */
		public boolean isModified();
	}

}
