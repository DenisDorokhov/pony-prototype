package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.entity.SongFile;
import net.dorokhov.pony.core.service.SongFileScanner;
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
import java.util.concurrent.*;

@Service
public class SongFileScannerImpl implements SongFileScanner {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private SongFileService songFileService;

	@Autowired
	public void setSongFileService(SongFileService aSongFileService) {
		songFileService = aSongFileService;
	}

	@Override
	public void scanFolders(Iterable<File> aFolders) {

		// TODO: count number of files scanned

		ExecutorService executor = Executors.newCachedThreadPool();

		for (File folder : aFolders) {
			scanFolderRecursively(folder, executor);
		}

		executor.shutdown();

		try {

			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

			// TODO: delete not touched files (modification date < scan start)

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean scanFolderRecursively(File aTarget, ExecutorService aExecutor) {

		if (aTarget.isDirectory()) {

			File[] subFiles = aTarget.listFiles();

			if (subFiles != null) {
				for (File file : subFiles) {
					if (!scanFolderRecursively(file, aExecutor)) {
						aExecutor.submit(new FileHandler(file));
					}
				}
			}

			return true;
		}

		return false;
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

			// TODO: check if song file exists

			SongFile songFile = new SongFile();

			songFile.setPath(target.getAbsolutePath());
			songFile.setType(header.getFormat());

			songFile.setSize(target.length());
			songFile.setDuration(header.getTrackLength());

			songFile.setDiscNumber(Integer.valueOf(tag.getFirst(FieldKey.DISC_NO)));
			songFile.setTrackNumber(Integer.valueOf(tag.getFirst(FieldKey.TRACK)));

			songFile.setName(tag.getFirst(FieldKey.TITLE));
			songFile.setAlbum(tag.getFirst(FieldKey.ALBUM));
			songFile.setArtist(tag.getFirst(FieldKey.ARTIST));
			songFile.setYear(Integer.valueOf(tag.getFirst(FieldKey.YEAR)));

			songFile = songFileService.save(songFile);

			log.debug("File [{}] successfully scanned.", target.getAbsolutePath());

			return songFile;
		}
	}
}
