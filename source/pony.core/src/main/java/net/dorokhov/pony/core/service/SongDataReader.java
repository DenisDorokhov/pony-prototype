package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.SongData;

import java.io.File;

public interface SongDataReader {

	public SongData readSongData(File aFile) throws Exception;

}
