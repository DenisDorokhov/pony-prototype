package net.dorokhov.pony.web.client.service;

import net.dorokhov.pony.web.shared.StatusDto;

public interface LibraryScanner {

	public static interface Delegate {

		public void onScanStarted(LibraryScanner aLibraryScanner);

		public void onScanProgress(LibraryScanner aLibraryScanner, StatusDto aStatus);

		public void onScanFailed(LibraryScanner aLibraryScanner, Throwable aCaught);

		public void onScanFinished(LibraryScanner aLibraryScanner);
	}

	public void addDelegate(Delegate aDelegate);

	public void removeDelegate(Delegate aDelegate);

	public boolean isScanning();

	public void scan();

	public StatusDto getLastStatus();

}
