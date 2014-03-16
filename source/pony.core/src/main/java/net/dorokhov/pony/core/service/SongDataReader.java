package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.SongArtwork;
import net.dorokhov.pony.core.domain.SongData;

import java.io.File;

public interface SongDataReader {

	public SongData readSongData(File aFile) throws Exception;

	public SongArtwork readSongArtwork(File aFile) throws Exception;

}
