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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LibraryScannerImpl implements LibraryScanner {

	private final static int NUMBER_OF_THREADS = 10;

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final Object lock = new Object();

	private final List<Delegate> delegates = new ArrayList<Delegate>();

	private final AtomicBoolean isScanning = new AtomicBoolean();
	private final AtomicBoolean isTrackingProgress = new AtomicBoolean();

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
	public boolean isScanning() {
		return isScanning.get();
	}

	@Override
	public Result scan(Iterable<File> aFiles) {

		if (isScanning()) {
			throw new RuntimeException("Concurrent scan.");
		}

		isScanning.set(true);
		isTrackingProgress.set(false);

		synchronized (lock) {
			for (Delegate next : delegates) {
				next.onScanStart();
			}
		}

		log.info("scanning library {}...", aFiles);

		long startTime = System.nanoTime();

		LibraryScannerResult result = new LibraryScannerResult();

		log.info("listing files...");

		List<File> filesToProcess = new ArrayList<File>();

		for (File file : aFiles) {
			if (file.exists()) {
				scanRecursively(file, filesToProcess, result);
			} else {
				log.error("file [{}] does not exist", file.getAbsolutePath());
			}
		}

		isTrackingProgress.set(true);

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

		isTrackingProgress.set(false);
		isScanning.set(false);

		synchronized (lock) {
			for (Delegate next : delegates) {
				next.onScanFinish(result);
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

		public double getProgress() {
			return (double) processedFilesCount.get() / scannedFilesCount.get();
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

			if (isTrackingProgress.get()) {
				synchronized (lock) {
					for (Delegate next : delegates) {
						next.onScanProgress(result.getProgress());
					}
				}
			}

			return songFile;
		}
	}
}
