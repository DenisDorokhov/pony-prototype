package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import net.dorokhov.pony.web.client.service.rpc.LibraryServiceRpcAsync;
import net.dorokhov.pony.web.shared.ScanStatusDto;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibraryScannerImpl implements LibraryScanner {

	private static final int STATUS_TIMER_INTERVAL_NOT_SCANNING = 15000;
	private static final int STATUS_TIMER_INTERVAL_SCANNING = 500;
	private static final int STATUS_TIMER_INTERVAL_FIRST = 500;

	private final Logger log = Logger.getLogger(getClass().getName());

	private final LibraryServiceRpcAsync libraryService;

	private final List<Delegate> delegates = new ArrayList<Delegate>();

	private boolean scanning;

	private ScanStatusDto lastStatus;

	private Timer statusTimer;

	@Inject
	public LibraryScannerImpl(LibraryServiceRpcAsync aLibraryService) {

		libraryService = aLibraryService;

		scheduleStatusTimer(STATUS_TIMER_INTERVAL_FIRST);
	}

	@Override
	public void addDelegate(Delegate aDelegate) {
		if (!delegates.contains(aDelegate)) {
			delegates.add(aDelegate);
		}
	}

	@Override
	public void removeDelegate(Delegate aDelegate) {
		delegates.remove(aDelegate);
	}

	@Override
	public boolean isScanning() {
		return scanning;
	}

	@Override
	public void scan() {
		if (!isScanning()) {
			doGetStatusAndScan();
		} else {
			log.warning("library is already scanning, skipping request");
		}
	}

	@Override
	public ScanStatusDto getLastStatus() {
		return lastStatus;
	}

	private void scheduleStatusTimer(int aTime) {

		if (statusTimer != null) {

			statusTimer.cancel();

			statusTimer = null;
		}

		statusTimer = new Timer() {
			@Override
			public void run() {

				statusTimer = null;

				updateStatus();
			}
		};

		statusTimer.schedule(aTime);
	}

	private void doGetStatusAndScan() {

		scanning = true;

		propagateScanStarted();

		log.fine("getting library status before scanning...");

		libraryService.getStatus(new AsyncCallback<ScanStatusDto>() {

			@Override
			public void onSuccess(ScanStatusDto aResult) {

				if (aResult == null) {

					log.fine("starting library scan...");

					doScan();

				} else {

					log.fine("library is already scanning, propagating progress...");

					propagateScanProgress(lastStatus);

					scheduleStatusTimer(STATUS_TIMER_INTERVAL_SCANNING);
				}
			}

			@Override
			public void onFailure(Throwable aCaught) {

				scanning = false;

				log.log(Level.SEVERE, "could not get library status before scanning", aCaught);

				propagateScanFailed(aCaught);
			}
		});
	}

	private void doScan() {
		libraryService.startScanning(new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void aResult) {
				scheduleStatusTimer(STATUS_TIMER_INTERVAL_SCANNING);
			}

			@Override
			public void onFailure(Throwable aCaught) {

				scanning = false;

				log.log(Level.SEVERE, "could not start scanning", aCaught);

				propagateScanFailed(aCaught);
			}
		});
	}

	private void updateStatus() {
		libraryService.getStatus(new AsyncCallback<ScanStatusDto>() {

			@Override
			public void onSuccess(ScanStatusDto aResult) {

				lastStatus = aResult;

				if (lastStatus != null) {

					if (!isScanning()) {

						scanning = true;

						propagateScanStarted();
					}

					propagateScanProgress(lastStatus);

					scheduleStatusTimer(STATUS_TIMER_INTERVAL_SCANNING);

				} else {

					if (isScanning()) {

						scanning = false;

						propagateScanFinished();
					}

					scheduleStatusTimer(STATUS_TIMER_INTERVAL_NOT_SCANNING);
				}
			}

			@Override
			public void onFailure(Throwable aCaught) {

				log.log(Level.SEVERE, "could not update library status", aCaught);

				scheduleStatusTimer(isScanning() ? STATUS_TIMER_INTERVAL_SCANNING : STATUS_TIMER_INTERVAL_NOT_SCANNING);
			}
		});
	}

	private void propagateScanStarted() {
		for (Delegate nextDelegate : new ArrayList<Delegate>(delegates)) {
			try {
				nextDelegate.onScanStarted(this);
			} catch (Exception e) {
				log.log(Level.SEVERE, "exception thrown when delegating onScanStarted to " + nextDelegate, e);
			}
		}
	}

	private void propagateScanProgress(ScanStatusDto aStatus) {
		for (Delegate nextDelegate : new ArrayList<Delegate>(delegates)) {
			try {
				nextDelegate.onScanProgress(this, aStatus);
			} catch (Exception e) {
				log.log(Level.SEVERE, "exception thrown when delegating onScanProgress to " + nextDelegate, e);
			}
		}
	}

	private void propagateScanFailed(Throwable aCaught) {
		for (Delegate nextDelegate : new ArrayList<Delegate>(delegates)) {
			try {
				nextDelegate.onScanFailed(this, aCaught);
			} catch (Exception e) {
				log.log(Level.SEVERE, "exception thrown when delegating onScanFailed to " + nextDelegate, e);
			}
		}
	}

	private void propagateScanFinished() {
		for (Delegate nextDelegate : new ArrayList<Delegate>(delegates)) {
			try {
				nextDelegate.onScanFinished(this);
			} catch (Exception e) {
				log.log(Level.SEVERE, "exception thrown when delegating onScanFinished to " + nextDelegate, e);
			}
		}
	}
}
