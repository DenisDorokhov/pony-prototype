package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.entity.SongFile;
import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.core.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.*;

@Service
public class LibraryScannerImpl implements LibraryScanner {

	private final static int NUMBER_OF_THREADS = 10;

	private final Logger log = LoggerFactory.getLogger(getClass());

	private LibraryService libraryService;

	@Autowired
	public void setLibraryService(LibraryService aLibraryService) {
		libraryService = aLibraryService;
	}

	@Override
	public Result scan(Iterable<File> aFiles) {

		log.info("scanning library {}", aFiles);

		long startTime = System.nanoTime();

		LibraryScannerResult result = new LibraryScannerResult();

		Date scanDate = new Date();

		ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

		for (File file : aFiles) {
			result.add(scanRecursively(file, executor));
		}

		executor.shutdown();

		try {

			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

			libraryService.clearSongFilesImportedBefore(scanDate);

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		long endTime = System.nanoTime();

		result.setDuration(endTime - startTime);

		log.info("library {} ({} folders, {} files) scanned successfully in {} seconds", aFiles, result.getScannedFoldersCount(), result.getScannedFilesCount(), result.getDuration() / 1000000000.0);

		return result;
	}

	@Override
	public Result scan(File aFile) {

		ArrayList<File> files = new ArrayList<File>();

		files.add(aFile);

		return scan(files);
	}

	private LibraryScannerResult scanRecursively(File aFile, ExecutorService aExecutor) {

		LibraryScannerResult result = new LibraryScannerResult();

		if (aFile.isDirectory()) {

			File[] subFiles = aFile.listFiles();

			if (subFiles != null) {
				for (File file : subFiles) {
					if (file.isDirectory()) {
						result.add(scanRecursively(file, aExecutor));
					} else {
						aExecutor.submit(new FileHandler(file, libraryService));
						result.incrementScannedFilesCount();
					}
				}
			}

			result.incrementScannedFoldersCount();

		} else {
			aExecutor.submit(new FileHandler(aFile, libraryService));
			result.incrementScannedFilesCount();
		}

		return result;
	}

	private class LibraryScannerResult implements Result {

		private long scannedFoldersCount = 0;

		private long scannedFilesCount = 0;

		private long duration = 0;

		@Override
		public long getScannedFoldersCount() {
			return scannedFoldersCount;
		}

		@Override
		public long getScannedFilesCount() {
			return scannedFilesCount;
		}

		public long getDuration() {
			return duration;
		}

		public void setDuration(long aDuration) {
			duration = aDuration;
		}

		public void incrementScannedFoldersCount() {
			scannedFoldersCount++;
		}

		public void incrementScannedFilesCount() {
			scannedFilesCount++;
		}

		public void add(Result aResult) {
			scannedFoldersCount += aResult.getScannedFoldersCount();
			scannedFilesCount += aResult.getScannedFilesCount();
		}
	}

	private class FileHandler implements Callable<SongFile> {

		private File file;

		private LibraryService libraryService;

		private FileHandler(File aFile, LibraryService aLibraryService) {
			file = aFile;
			libraryService = aLibraryService;
		}

		@Override
		public SongFile call() throws Exception {
			return libraryService.importSongFile(file);
		}
	}
}
