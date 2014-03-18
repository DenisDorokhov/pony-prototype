package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.SongFile;

import java.io.File;

public interface LibraryService {

	public SongFile importSong(File aFile);

	public void cleanDeletedSongs(ProgressHandler aHandler);
	public void cleanNotUsedFiles(ProgressHandler aHandler);
	public void cleanNotUsedAlbums(ProgressHandler aHandler);
	public void cleanNotUsedArtists(ProgressHandler aHandler);

	public interface ProgressHandler {
		public void handleProgress(double aProgress);
	}

}
