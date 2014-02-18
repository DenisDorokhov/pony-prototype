package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.entity.SongFile;
import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.core.service.SongFileService;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
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

	private final static int NUMBER_OF_THREADS = 4;

	private final Logger log = LoggerFactory.getLogger(getClass());

	private SongFileService songFileService;

	@Autowired
	public void setSongFileService(SongFileService aSongFileService) {
		songFileService = aSongFileService;
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

			songFileService.deleteUpdatedBefore(scanDate);

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
						aExecutor.submit(new FileHandler(file));
						result.incrementScannedFilesCount();
					}
				}
			}

			result.incrementScannedFoldersCount();

		} else {
			aExecutor.submit(new FileHandler(aFile));
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

		private File target;

		private FileHandler(File aTarget) {
			target = aTarget;
		}

		@Override
		public SongFile call() throws Exception {

			AudioFile file = AudioFileIO.read(target);

			AudioHeader header = file.getAudioHeader();
			Tag tag = file.getTag();

			SongFile songFile = null;

			try {

				songFile = songFileService.getByPath(target.getAbsolutePath());

				if (songFile == null) {
					songFile = new SongFile();
				}

				songFile.setPath(target.getAbsolutePath());
				songFile.setType(header.getFormat());
				songFile.setSize(target.length());

				songFile.setDuration(header.getTrackLength());
				songFile.setBitRate(header.getBitRateAsNumber());

				songFile.setDiscNumber(parseIntTag(tag, FieldKey.DISC_NO));
				songFile.setDiscCount(parseIntTag(tag, FieldKey.DISC_TOTAL));

				songFile.setTrackNumber(parseIntTag(tag, FieldKey.TRACK));
				songFile.setTrackCount(parseIntTag(tag, FieldKey.TRACK_TOTAL));

				songFile.setName(tag.getFirst(FieldKey.TITLE));
				songFile.setAlbum(tag.getFirst(FieldKey.ALBUM));
				songFile.setArtist(tag.getFirst(FieldKey.ARTIST));
				songFile.setYear(Integer.valueOf(tag.getFirst(FieldKey.YEAR)));

				songFile = songFileService.save(songFile);

				log.debug("File [{}] successfully scanned.", target.getAbsolutePath());

			} catch (Exception e) {
				log.error("File [{}] could not be scanned.", target.getAbsolutePath(), e);
			}

			return songFile;
		}

		private Integer parseIntTag(Tag aTag, FieldKey aKey) {

			Integer result = null;

			try {
				result = Integer.valueOf(aTag.getFirst(aKey));
			} catch (NumberFormatException e) {}

			return result;
		}
	}
}
