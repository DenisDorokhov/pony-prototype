package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.SongFile;

import java.io.File;
import java.util.List;

public interface LibraryService {

	public SongFile importSong(File aFile);

	public void normalizeSongs(List<File> aTargetFiles, ProgressHandler aHandler);

	public void normalizeStoredFiles(ProgressHandler aHandler);
	public void normalizeAlbums(ProgressHandler aHandler);
	public void normalizeArtists(ProgressHandler aHandler);

	public interface ProgressHandler {
		public void handleProgress(double aProgress);
	}

}
