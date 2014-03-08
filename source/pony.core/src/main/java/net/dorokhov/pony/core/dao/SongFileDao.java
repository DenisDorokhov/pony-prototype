package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.SongFile;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SongFileDao extends PagingAndSortingRepository<SongFile, Integer> {

	public SongFile findByPath(String aPath);

}
