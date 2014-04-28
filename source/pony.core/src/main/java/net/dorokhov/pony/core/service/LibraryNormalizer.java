package net.dorokhov.pony.core.service;

import java.io.File;
import java.util.List;

/**
 * Library normalization service.
 *
 * Validates library entities and fixes data inconsistencies. Because of entity hierarchy methods are supposed to be
 * called in the following order:
 *
 * 1) normalizeSongs()
 * 2) normalizeStoredFiles()
 * 3) normalizeAlbums()
 * 4) normalizeArtists()
 */
public interface LibraryNormalizer {

	/**
	 * Normalizes song files.
	 *
	 * This method will delete all files that don't belong to passed target files / folders or doesn't point to existing
	 * files in the filesystem anymore. Additional normalization process fully depends on particular implementation.
	 *
	 * @param aTargetFiles target files (song files that don't belong to target files / folders will be deleted).
	 * @param aHandler operation progress handler
	 */
	public void normalizeSongs(List<File> aTargetFiles, ProgressHandler aHandler);

	/**
	 * Normalizes stored files.
	 *
	 * This method will delete stored files that are not referenced by any entities. Additional normalization process
	 * fully depends on particular implementation.
	 *
	 * @param aHandler operation progress handler
	 */
	public void normalizeStoredFiles(ProgressHandler aHandler);

	/**
	 * Normalizes albums.
	 *
	 * This method will delete albums without songs (e.g. when songs were deleted during song normalization).
	 * Additional normalization process fully depends on particular implementation.
	 *
	 * @param aHandler operation progress handler
	 */
	public void normalizeAlbums(ProgressHandler aHandler);

	/**
	 * Normalizes artists.
	 *
	 * This method will delete artists without albums (e.g. when albums were deleted during album normalization).
	 * Additional normalization process fully depends on particular implementation.
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
