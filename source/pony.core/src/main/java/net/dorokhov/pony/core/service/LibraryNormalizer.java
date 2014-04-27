package net.dorokhov.pony.core.service;

import java.io.File;
import java.util.List;

/**
 * Library normalization service.
 *
 * Validates library entities and fixes data inconsistencies.
 */
public interface LibraryNormalizer {

	/**
	 * Normalizes song files.
	 *
	 * This method will delete all files that don't belong to passed target files. It is needed when library folder
	 * configuration have been changed. Example of additional normalization can be deletion of song file entities that
	 * doesn't point to an existing file anymore. Song normalization process fully depends on particular implementation.
	 *
	 * @param aTargetFiles target files (song files that don't belong to target files will be deleted).
	 * @param aHandler operation progress handler
	 */
	public void normalizeSongs(List<File> aTargetFiles, ProgressHandler aHandler);

	/**
	 * Normalizes stored files.
	 *
	 * Example of such normalization can be deletion of stored files that are not referenced by any entities.
	 *
	 * @param aHandler operation progress handler
	 */
	public void normalizeStoredFiles(ProgressHandler aHandler);

	/**
	 * Normalizes albums.
	 *
	 * Example of such normalization can be deletion of albums without songs (e.g. when songs were deleted during song
	 * normalization).
	 *
	 * @param aHandler operation progress handler
	 */
	public void normalizeAlbums(ProgressHandler aHandler);

	/**
	 * Normalizes artists.
	 *
	 * Example of such normalization can be deletion of artists without albums (e.g. when albums were deleted during
	 * album normalization).
	 *
	 * @param aHandler operation progress handler
	 */
	public void normalizeArtists(ProgressHandler aHandler);

	/**
	 * Library service operation progress handler.
	 */
	public interface ProgressHandler {

		/**
		 * This method is called when operation progress changes.
		 *
		 * @param aProgress operation progress (from 0.0 to 1.0)
		 */
		public void handleProgress(double aProgress);

	}

}
