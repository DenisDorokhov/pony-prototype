package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.exception.ConcurrentScanException;

import java.io.File;
import java.util.List;

/**
 * Library scanner.
 *
 * Implementation is supposed to be multi-threaded, therefore implementation must be thread-safe.
 */
public interface LibraryScanner {

	/**
	 * Adds library scanner delegate.
	 *
	 * @param aDelegate delegate to add
	 */
	public void addDelegate(Delegate aDelegate);

	/**
	 * Removes library scanner delegate.
	 *
	 * @param aDelegate delegate to remove
	 */
	public void removeDelegate(Delegate aDelegate);

	/**
	 * Gets library scanner status
	 *
	 * @return library scanner status or null if library is not being scanned
	 */
	public Status getStatus();

	/**
	 * Scans library files recursively.
	 *
	 * @param aTargetFiles files to scan
	 * @return library scanning result
	 * @throws ConcurrentScanException in case library is already being scanned
	 */
	public Result scan(List<File> aTargetFiles) throws ConcurrentScanException;

	/**
	 * Library scanning result.
	 */
	public static interface Result {

		/**
		 * Gets scanned target files.
		 *
		 * @return scanned target files
		 */
		public List<File> getTargetFiles();

		/**
		 * Gets scanned folders count.
		 *
		 * @return number of scanned folders
		 */
		public long getScannedFoldersCount();

		/**
		 * Gets scanned files count.
		 *
		 * @return number of scanned files
		 */
		public long getScannedFilesCount();

		/**
		 * Gets imported files count.
		 *
		 * @return number of imported files
		 */
		public long getImportedFilesCount();

		/**
		 * Gets library scanning duration.
		 *
		 * @return library scanning duration
		 */
		public long getDuration();

	}

	/**
	 * Library scanning status.
	 */
	public static interface Status {

		/**
		 * Gets library scanning target files.
		 *
		 * @return library scanning target files
		 */
		public List<File> getTargetFiles();

		/**
		 * Get library scanning status description.
		 *
		 * @return library scanning status description
		 */
		public String getDescription();

		/**
		 * Gets library scanning step progress (from 0.0 to 1.0).
		 *
		 * @return library scanning progress
		 */
		public double getProgress();

		/**
		 * Gets current library scanning step number (first step number is 1).
		 *
		 * @return current library scanning step number
		 */
		public int getStep();

		/**
		 * Gets number of library scanning steps.
		 *
		 * @return number of library scanning steps
		 */
		public int getTotalSteps();
	}

	/**
	 * Library scanner delegate.
	 */
	public static interface Delegate {

		/**
		 * This method is called when scanning starts.
		 */
		public void onScanStart();

		/**
		 * This method is called when scanning progress has been changed.
		 *
		 * @param aStatus library scanning status
		 */
		public void onScanProgress(Status aStatus);

		/**
		 * This method is called when scanning successfully finishes.
		 *
		 * @param aResult library scanning result
		 */
		public void onScanFinish(Result aResult);

		/**
		 * This method is called when library scanning fails.
		 *
		 * @param e fail reason
		 */
		public void onScanFail(Exception e);
	}

}
