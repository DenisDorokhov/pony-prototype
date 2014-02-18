package net.dorokhov.pony.core.service;

import java.io.File;

public interface LibraryScanner {

	public Result scan(Iterable<File> aFiles);

	public Result scan(File aFile);

	public static interface Result {

		public long getScannedFoldersCount();

		public long getScannedFilesCount();

		public long getDuration();

	}

}
