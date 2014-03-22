package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.Song;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SongDaoCustom {

	List<Song> search(String aText, Pageable aPageable);

}
