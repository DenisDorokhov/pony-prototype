package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.entity.Song;
import net.dorokhov.pony.core.entity.SongFile;

public interface LibraryService {

	public Song importSongFile(SongFile aSongFile);

}
