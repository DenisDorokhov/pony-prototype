package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.SongFile;

import java.io.File;

public interface LibraryService {

	public SongFile importSongFile(File aFile);

	public void cleanUpSongFiles();

}
