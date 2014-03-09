package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.domain.SongFile;
import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.core.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class LibraryScannerImpl implements LibraryScanner {

	private final static int NUMBER_OF_THREADS = 10;

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final Object lock = new Object();

	private final List<Delegate> delegates = new ArrayList<Delegate>();

	private final AtomicReference<Double> progress = new AtomicReference<Double>();

	private LibraryService libraryService;

	@Autowired
	public void setLibraryService(LibraryService aLibraryService) {
		libraryService = aLibraryService;
	}

	@Override
	public void addDelegate(Delegate aDelegate) {
		synchronized (lock) {
			if (!delegates.contains(aDelegate)) {
				delegates.add(aDelegate);
			}
		}
	}

	@Override
	public void removeDelegate(Delegate aDelegate) {
		synchronized (lock) {
			delegates.remove(aDelegate);
		}
	}

	@Override
	public Status getStatus() {

		Double progressValue = progress.get();

		return new LibraryScannerStatus(progressValue != null, progressValue != null ? progressValue : 0.0);
	}

	@Override
	public Result scan(Iterable<File> aFiles) {

		if (progress.get() != null) {
			throw new RuntimeException("Concurrent scan.");
		}

		progress.set(0.0);

		synchronized (lock) {
			for (Delegate next : delegates) {
				try {
					next.onScanStart();
				} catch (Exception e) {
					log.error("exception thrown when delegating onScanStart", e);
				}
			}
		}

		log.info("scanning library {}...", aFiles);

		long startTime = System.nanoTime();

		log.info("listing files...");

		LibraryScannerResult result = new LibraryScannerResult();

		List<File> filesToProcess = new ArrayList<File>();

		for (File file : aFiles) {
			if (file.exists()) {
				scanRecursively(file, filesToProcess, result);
			} else {
				log.error("file [{}] does not exist", file.getAbsolutePath());
			}
		}

		log.info("processing files...");

		ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

		for (File file : filesToProcess) {
			executor.submit(new FileProcessor(file, result));
		}

		executor.shutdown();

		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		log.info("checking files for deletion...");

		libraryService.cleanUpSongFiles();

		long endTime = System.nanoTime();

		result.setDuration(endTime - startTime);

		log.info("library {} ({} folders, {} files) scanned successfully in {} seconds", aFiles, result.getScannedFoldersCount(), result.getScannedFilesCount(), result.getDuration() / 1000000000.0);

		progress.set(null);

		synchronized (lock) {
			for (Delegate next : delegates) {
				try {
					next.onScanFinish(result);
				} catch (Exception e) {
					log.error("exception thrown when delegating onScanFinish", e);
				}
			}
		}

		return result;
	}

	@Override
	public Result scan(File aFile) {

		ArrayList<File> files = new ArrayList<File>();

		files.add(aFile);

		return scan(files);
	}

	private void scanRecursively(File aFile, List<File> aFilesToProcess, LibraryScannerResult aResult) {

		if (aFile.isDirectory()) {

			File[] subFiles = aFile.listFiles();

			if (subFiles != null) {

				Arrays.sort(subFiles);

				for (File file : subFiles) {
					if (file.isDirectory()) {
						scanRecursively(file, aFilesToProcess, aResult);
					} else {
						aFilesToProcess.add(file);
						aResult.incrementScannedFilesCount();
					}
				}
			}

			aResult.incrementScannedFoldersCount();

		} else {
			aFilesToProcess.add(aFile);
			aResult.incrementScannedFilesCount();
		}
	}

	private static class LibraryScannerResult implements Result {

		private AtomicLong scannedFoldersCount = new AtomicLong();
		private AtomicLong scannedFilesCount = new AtomicLong();
		private AtomicLong processedFilesCount = new AtomicLong();
		private AtomicLong duration = new AtomicLong();

		@Override
		public long getScannedFoldersCount() {
			return scannedFoldersCount.get();
		}

		@Override
		public long getScannedFilesCount() {
			return scannedFilesCount.get();
		}

		@Override
		public long getDuration() {
			return duration.get();
		}

		public void setDuration(long aDuration) {
			duration.set(aDuration);
		}

		public void incrementScannedFoldersCount() {
			scannedFoldersCount.incrementAndGet();
		}

		public void incrementScannedFilesCount() {
			scannedFilesCount.incrementAndGet();
		}

		public void incrementProcessedFilesCount() {
			processedFilesCount.incrementAndGet();
		}

		public double calculateProgress() {
			return (double) processedFilesCount.get() / scannedFilesCount.get();
		}
	}

	private static class LibraryScannerStatus implements Status {

		private boolean scanning;

		private double progress;

		private LibraryScannerStatus(boolean aScanning, double aProgress) {
			scanning = aScanning;
			progress = aProgress;
		}

		@Override
		public boolean isScanning() {
			return scanning;
		}

		@Override
		public double getProgress() {
			return progress;
		}
	}

	private class FileProcessor implements Callable<SongFile> {

		private File file;

		private LibraryScannerResult result;

		public FileProcessor(File aFile, LibraryScannerResult aResult) {
			file = aFile;
			result = aResult;
		}

		@Override
		public SongFile call() throws Exception {

			SongFile songFile = null;

			try {
				songFile = libraryService.importSongFile(file);
			} catch (Exception e) {}

			result.incrementProcessedFilesCount();

			progress.set(result.calculateProgress());

			synchronized (lock) {
				for (Delegate next : delegates) {
					try {
						next.onScanProgress(result.calculateProgress());
					} catch (Exception e) {
						log.error("exception thrown when delegating onScanProgress", e);
					}
				}
			}

			return songFile;
		}
	}
}
