package net.dorokhov.pony.core.service;

import java.io.File;

public interface SongFileScanner {

	public void scanFolders(Iterable<File> aFolders);

}
