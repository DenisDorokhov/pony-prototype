package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.exception.ConcurrentScanException;

import java.io.File;
import java.util.List;

public interface LibraryScanner {

	public void addDelegate(Delegate aDelegate);

	public void removeDelegate(Delegate aDelegate);

	public Status getStatus();

	public Result scan(List<File> aTargetFiles) throws ConcurrentScanException;

	public Result scan(File aTargetFile) throws ConcurrentScanException;

	public static interface Result {

		public List<File> getTargetFiles();

		public long getScannedFoldersCount();
		public long getScannedFilesCount();
		public long getImportedFilesCount();

		public long getDuration();

	}

	public static interface Status {

		public List<File> getTargetFiles();

		public String getDescription();

		public double getProgress();
		public int getStep();
		public int getTotalSteps();
	}

	public static interface Delegate {

		public void onScanStart();

		public void onScanProgress(Status aStatus);

		public void onScanFinish(Result aResult);

		public void onScanFail(Exception e);
	}

}
