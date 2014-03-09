package net.dorokhov.pony.core.service;

import java.io.File;
import java.util.List;

public interface LibraryScanner {

	public void addDelegate(Delegate aDelegate);

	public void removeDelegate(Delegate aDelegate);

	public Status getStatus();

	public Result scan(List<File> aFiles);

	public Result scan(File aFile);

	public static interface Result {

		public List<File> getScanningFiles();

		public long getScannedFoldersCount();

		public long getScannedFilesCount();

		public long getDuration();

	}

	public static interface Status {

		public boolean isScanning();

		public List<File> getScanningFiles();

		public Double getProgress();
	}

	public static interface Delegate {

		public void onScanStart();

		public void onScanProgress(Status aStatus);

		public void onScanFinish(Result aResult);
	}

}
