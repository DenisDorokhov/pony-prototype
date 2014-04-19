package net.dorokhov.pony.core.service.library;

import net.dorokhov.pony.core.domain.SongFile;
import net.dorokhov.pony.core.exception.ConcurrentScanException;
import net.dorokhov.pony.core.service.LibraryNormalizer;
import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.core.service.LibraryImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
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
	private final static int NUMBER_OF_STEPS = 6;

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Object lockDelegates = new Object();

	private final List<Delegate> delegates = new ArrayList<Delegate>();

	private final AtomicReference<LibraryScannerStatus> statusReference = new AtomicReference<LibraryScannerStatus>();
	private final AtomicReference<ExecutorService> executorServiceReference = new AtomicReference<ExecutorService>();
	private final AtomicLong processedFilesCountReference = new AtomicLong();

	private LibraryImporter libraryImporter;

	private LibraryNormalizer libraryNormalizer;

	@Autowired
	public void setLibraryImporter(LibraryImporter aLibraryImporter) {
		libraryImporter = aLibraryImporter;
	}

	@Autowired
	public void setLibraryNormalizer(LibraryNormalizer aLibraryNormalizer) {
		libraryNormalizer = aLibraryNormalizer;
	}

	@Override
	public void addDelegate(Delegate aDelegate) {
		synchronized (lockDelegates) {
			if (!delegates.contains(aDelegate)) {
				delegates.add(aDelegate);
			}
		}
	}

	@Override
	public void removeDelegate(Delegate aDelegate) {
		synchronized (lockDelegates) {
			delegates.remove(aDelegate);
		}
	}

	@Override
	public Status getStatus() {

		Status status = statusReference.get();

		return status != null ? new LibraryScannerStatus(status) : null;
	}

	@Override
	@Transactional
	public Result scan(List<File> aTargetFiles) throws ConcurrentScanException {

		if (statusReference.get() != null) {
			throw new ConcurrentScanException();
		}

		statusReference.set(new LibraryScannerStatus(aTargetFiles, "preparing", 0.0, 1));

		synchronized (lockDelegates) {
			for (Delegate next : delegates) {
				try {
					next.onScanStart();
				} catch (Exception e) {
					log.error("exception thrown when delegating onScanStart to {}", next, e);
				}
			}
		}

		LibraryScannerResult result = new LibraryScannerResult(aTargetFiles);

		try {

			executorServiceReference.set(Executors.newFixedThreadPool(NUMBER_OF_THREADS));
			processedFilesCountReference.set(0);

			doScan(aTargetFiles, result);

			synchronized (lockDelegates) {
				for (Delegate next : delegates) {
					try {
						next.onScanFinish(result);
					} catch (Exception e) {
						log.error("exception thrown when delegating onScanFinish to {}", next, e);
					}
				}
			}

		} catch (Exception processingException) {

			synchronized (lockDelegates) {
				for (Delegate next : delegates) {
					try {
						next.onScanFail(processingException);
					} catch (Exception e) {
						log.error("exception thrown when delegating onScanFail to {}", next, e);
					}
				}
			}

			throw new RuntimeException(processingException);

		} finally {

			executorServiceReference.set(null);
			statusReference.set(null);
			processedFilesCountReference.set(0);
		}

		log.info("library {} ({} folders, {} files) scanned successfully, {} files imported in {} seconds",
				result.getTargetFiles(), result.getScannedFoldersCount(), result.getScannedFilesCount(), result.getImportedFilesCount(), result.getDuration() / 1000000000.0);

		return result;
	}

	@Override
	@Transactional
	public Result scan(File aTargetFile) throws ConcurrentScanException {

		ArrayList<File> files = new ArrayList<File>();

		files.add(aTargetFile);

		return scan(files);
	}

	@PreDestroy
	public void onPreDestroy() {

		ExecutorService executor = executorServiceReference.get();

		if (executor != null) {
			executor.shutdownNow();
		}
	}

	private void doScan(final List<File> aTargetFiles, final LibraryScannerResult aResult) {

		log.info("scanning library {}...", aTargetFiles);

		long startTime = System.nanoTime();

		log.info("preparing...");

		List<File> filesToProcess = new ArrayList<File>();

		for (File file : aTargetFiles) {
			if (file.exists()) {
				scanRecursively(file, filesToProcess, aResult);
			} else {
				log.error("file [{}] does not exist", file.getAbsolutePath());
			}
		}

		log.info("importing songs...");

		ExecutorService executor = executorServiceReference.get();

		for (File file : filesToProcess) {
			executor.submit(new FileProcessor(file, aResult));
		}

		executor.shutdown();

		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		log.info("normalizing songs...");
		libraryNormalizer.normalizeSongs(aTargetFiles, new LibraryNormalizer.ProgressHandler() {
			@Override
			public void handleProgress(double aProgress) {
				statusReference.set(new LibraryScannerStatus(aTargetFiles, "normalizingSongs", aProgress, 3));
			}
		});

		log.info("normalizing stored files...");
		libraryNormalizer.normalizeStoredFiles(new LibraryNormalizer.ProgressHandler() {
			@Override
			public void handleProgress(double aProgress) {
				statusReference.set(new LibraryScannerStatus(aTargetFiles, "normalizingStoredFiles", aProgress, 4));
			}
		});

		log.info("normalizing albums...");
		libraryNormalizer.normalizeAlbums(new LibraryNormalizer.ProgressHandler() {
			@Override
			public void handleProgress(double aProgress) {
				statusReference.set(new LibraryScannerStatus(aTargetFiles, "normalizingAlbums", aProgress, 5));
			}
		});

		log.info("normalizing artists...");
		libraryNormalizer.normalizeArtists(new LibraryNormalizer.ProgressHandler() {
			@Override
			public void handleProgress(double aProgress) {
				statusReference.set(new LibraryScannerStatus(aTargetFiles, "normalizingArtists", aProgress, 6));
			}
		});

		long endTime = System.nanoTime();

		aResult.setDuration(endTime - startTime);
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

		private final List<File> targetFiles;

		private final AtomicLong scannedFoldersCount = new AtomicLong();
		private final AtomicLong scannedFilesCount = new AtomicLong();
		private final AtomicLong importedFilesCount = new AtomicLong();
		private final AtomicLong duration = new AtomicLong();

		public LibraryScannerResult(List<File> aTargetFiles) {
			targetFiles = new ArrayList<File>(aTargetFiles);
		}

		@Override
		public List<File> getTargetFiles() {
			return targetFiles;
		}

		@Override
		public long getScannedFoldersCount() {
			return scannedFoldersCount.get();
		}

		@Override
		public long getScannedFilesCount() {
			return scannedFilesCount.get();
		}

		@Override
		public long getImportedFilesCount() {
			return importedFilesCount.get();
		}

		@Override
		public long getDuration() {
			return duration.get();
		}

		public void setDuration(long aDuration) {
			duration.set(aDuration);
		}

		public long incrementScannedFoldersCount() {
			return scannedFoldersCount.incrementAndGet();
		}

		public long incrementScannedFilesCount() {
			return scannedFilesCount.incrementAndGet();
		}

		public long incrementImportedFilesCount() {
			return importedFilesCount.incrementAndGet();
		}
	}

	private static class LibraryScannerStatus implements Status {

		private final List<File> targetFiles;
		private final String description;
		private final double progress;
		private final int step;

		public LibraryScannerStatus(List<File> aTargetFiles, String aDescription, double aProgress, int aStep) {
			targetFiles = aTargetFiles != null ? new ArrayList<File>(aTargetFiles) : null;
			description = aDescription;
			progress = aProgress;
			step = aStep;
		}

		public LibraryScannerStatus(Status aStatus) {
			this(aStatus.getTargetFiles(), aStatus.getDescription(), aStatus.getProgress(), aStatus.getStep());
		}

		@Override
		public List<File> getTargetFiles() {
			return targetFiles != null ? new ArrayList<File>(targetFiles) : null;
		}

		@Override
		public String getDescription() {
			return description;
		}

		@Override
		public double getProgress() {
			return progress;
		}

		@Override
		public int getStep() {
			return step;
		}

		@Override
		public int getTotalSteps() {
			return NUMBER_OF_STEPS;
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
				songFile = libraryImporter.importSong(file);
			} catch (Exception e) {}

			if (songFile != null) {
				result.incrementImportedFilesCount();
			}

			long processedFilesCount = processedFilesCountReference.incrementAndGet();

			double progress = (double) processedFilesCount / result.getScannedFilesCount();

			statusReference.set(new LibraryScannerStatus(result.getTargetFiles(), "importingSongs", progress, 2));

			synchronized (lockDelegates) {
				for (Delegate next : delegates) {
					try {
						next.onScanProgress(getStatus());
					} catch (Exception e) {
						log.error("exception thrown when delegating onScanProgress", e);
					}
				}
			}

			Thread.sleep(50); // avoid high CPU load

			return songFile;
		}
	}
}