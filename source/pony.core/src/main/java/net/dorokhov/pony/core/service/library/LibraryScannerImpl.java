package net.dorokhov.pony.core.service.library;

import net.dorokhov.pony.core.dao.ScanResultDao;
import net.dorokhov.pony.core.domain.ScanResult;
import net.dorokhov.pony.core.domain.SongFile;
import net.dorokhov.pony.core.exception.ConcurrentScanException;
import net.dorokhov.pony.core.service.LibraryImporter;
import net.dorokhov.pony.core.service.LibraryNormalizer;
import net.dorokhov.pony.core.service.LibraryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

	private TransactionTemplate transactionTemplate;

	private ScanResultDao scanResultDao;

	private LibraryImporter libraryImporter;

	private LibraryNormalizer libraryNormalizer;

	@Autowired
	public void setTransactionManager(PlatformTransactionManager aTransactionManager) {
		transactionTemplate = new TransactionTemplate(aTransactionManager, new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
	}

	@Autowired
	public void setScanResultDao(ScanResultDao aScanResultDao) {
		scanResultDao = aScanResultDao;
	}

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
	@Transactional(readOnly = true)
	public ScanResult getLastResult() {

		Page<ScanResult> scanResults = scanResultDao.findAll(new PageRequest(0, 1, Sort.Direction.DESC, "date"));

		return scanResults.getNumberOfElements() > 0 ? scanResults.getContent().get(0) : null;
	}

	@Override
	@Transactional
	public ScanResult scan(List<File> aTargetFiles) throws ConcurrentScanException {

		if (statusReference.get() != null) {
			throw new ConcurrentScanException();
		}

		statusReference.set(new LibraryScannerStatus(aTargetFiles, "preparing", 0.0, 1));

		synchronized (lockDelegates) {
			for (Delegate next : new ArrayList<Delegate>(delegates)) {
				try {
					next.onScanStart();
				} catch (Exception e) {
					log.error("exception thrown when delegating onScanStart to {}", next, e);
				}
			}
		}

		ScanResult scanResult;

		final AtomicResult atomicResult = new AtomicResult(aTargetFiles);

		try {

			executorServiceReference.set(Executors.newFixedThreadPool(NUMBER_OF_THREADS));
			processedFilesCountReference.set(0);

			doScan(aTargetFiles, atomicResult);

			scanResult = scanResultDao.save(buildScanResult(atomicResult, true));

			synchronized (lockDelegates) {
				for (Delegate next : new ArrayList<Delegate>(delegates)) {
					try {
						next.onScanFinish(scanResult);
					} catch (Exception e) {
						log.error("exception thrown when delegating onScanFinish to {}", next, e);
					}
				}
			}

		} catch (Exception processingException) {

			scanResult = buildScanResult(atomicResult, false);

			try {

				final ScanResult resultToSave = scanResult;

				scanResult = transactionTemplate.execute(new TransactionCallback<ScanResult>() {
					@Override
					public ScanResult doInTransaction(TransactionStatus aStatus) {
						return scanResultDao.save(resultToSave);
					}
				});

			} catch (Exception e) {
				log.error("could not save scan result", e);
			}

			synchronized (lockDelegates) {
				for (Delegate next : new ArrayList<Delegate>(delegates)) {
					try {
						next.onScanFail(scanResult, processingException);
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
				scanResult.getTargetFiles(),
				scanResult.getScannedFolderCount(), scanResult.getScannedFileCount(), scanResult.getImportedFileCount(),
				scanResult.getDuration() / 1000000000.0);

		return scanResult;
	}

	@PreDestroy
	public void onPreDestroy() {

		ExecutorService executor = executorServiceReference.get();

		if (executor != null) {
			executor.shutdownNow();
		}
	}

	private void doScan(final List<File> aTargetFiles, final AtomicResult aResult) {

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

	private void scanRecursively(File aFile, List<File> aFilesToProcess, AtomicResult aResult) {

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

	private ScanResult buildScanResult(AtomicResult aResult, boolean aSuccess) {

		ScanResult scanResult = new ScanResult();

		scanResult.setDate(new Date());
		scanResult.setSuccess(aSuccess);

		for (File file : aResult.getTargetFiles()) {
			scanResult.getTargetFiles().add(file.getAbsolutePath());
		}

		scanResult.setDuration(aResult.getDuration());
		scanResult.setScannedFolderCount(aResult.getScannedFolderCount());
		scanResult.setScannedFileCount(aResult.getScannedFileCount());
		scanResult.setImportedFileCount(aResult.getImportedFileCount());

		return scanResult;
	}

	private static class AtomicResult {

		private final List<File> targetFiles;

		private final AtomicLong scannedFolderCount = new AtomicLong();
		private final AtomicLong scannedFileCount = new AtomicLong();
		private final AtomicLong importedFileCount = new AtomicLong();

		private final AtomicLong duration = new AtomicLong();

		public AtomicResult(List<File> aTargetFiles) {
			targetFiles = new ArrayList<File>(aTargetFiles);
		}

		public List<File> getTargetFiles() {
			return targetFiles;
		}

		public long getScannedFolderCount() {
			return scannedFolderCount.get();
		}

		public long getScannedFileCount() {
			return scannedFileCount.get();
		}

		public long getImportedFileCount() {
			return importedFileCount.get();
		}

		public long getDuration() {
			return duration.get();
		}

		public void setDuration(long aDuration) {
			duration.set(aDuration);
		}

		public long incrementScannedFoldersCount() {
			return scannedFolderCount.incrementAndGet();
		}

		public long incrementScannedFilesCount() {
			return scannedFileCount.incrementAndGet();
		}

		public long incrementImportedFilesCount() {
			return importedFileCount.incrementAndGet();
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

		private AtomicResult result;

		public FileProcessor(File aFile, AtomicResult aResult) {
			file = aFile;
			result = aResult;
		}

		@Override
		public SongFile call() throws Exception {

			LibraryImporter.Result importResult = null;

			try {
				importResult = libraryImporter.importSong(file);
			} catch (Exception e) {}

			if (importResult != null && importResult.isModified()) {
				result.incrementImportedFilesCount();
			}

			long processedFilesCount = processedFilesCountReference.incrementAndGet();

			double progress = (double) processedFilesCount / result.getScannedFileCount();

			statusReference.set(new LibraryScannerStatus(result.getTargetFiles(), "importingSongs", progress, 2));

			synchronized (lockDelegates) {
				for (Delegate next : new ArrayList<Delegate>(delegates)) {
					try {
						next.onScanProgress(getStatus());
					} catch (Exception e) {
						log.error("exception thrown when delegating onScanProgress", e);
					}
				}
			}

			Thread.sleep(50); // avoid high CPU load

			return importResult != null ? importResult.getFile() : null;
		}
	}
}
