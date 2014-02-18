package net.dorokhov.pony.core.service;

import java.io.File;

public interface LibraryScanner {

	public Result scan(Iterable<File> aFolders);

	public static interface Result {

		public long getScannedFoldersCount();

		public long getScannedFilesCount();

	}

}
