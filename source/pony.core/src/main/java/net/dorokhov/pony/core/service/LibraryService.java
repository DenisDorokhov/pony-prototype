package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.entity.SongFile;

import java.io.File;
import java.util.Date;

public interface LibraryService {

	public SongFile importSongFile(File aFile) throws Exception;

	public void clearSongFilesImportedBefore(Date aDate);

}
