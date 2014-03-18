package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.SongFile;

import java.io.File;
import java.util.List;

public interface LibraryService {

	public SongFile importSong(File aFile);

	public void cleanSongs(List<File> aTargetFiles, ProgressHandler aHandler);

	public void cleanStoredFiles(ProgressHandler aHandler);
	public void cleanAlbums(ProgressHandler aHandler);
	public void cleanArtists(ProgressHandler aHandler);

	public interface ProgressHandler {
		public void handleProgress(double aProgress);
	}

}
