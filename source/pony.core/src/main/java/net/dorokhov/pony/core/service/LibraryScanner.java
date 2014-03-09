package net.dorokhov.pony.core.service;

import java.io.File;

public interface LibraryScanner {

	public void addDelegate(Delegate aDelegate);

	public void removeDelegate(Delegate aDelegate);

	public Status getStatus();

	public Result scan(Iterable<File> aFiles);

	public Result scan(File aFile);

	public static interface Result {

		public long getScannedFoldersCount();

		public long getScannedFilesCount();

		public long getDuration();

	}

	public static interface Status {

		public boolean isScanning();

		public double getProgress();
	}

	public static interface Delegate {

		public void onScanStart();

		public void onScanProgress(double aProgress);

		public void onScanFinish(Result aResult);
	}

}
