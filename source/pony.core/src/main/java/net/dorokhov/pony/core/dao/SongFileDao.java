package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.entity.SongFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface SongFileDao extends PagingAndSortingRepository<SongFile, Integer> {

	public SongFile findByPath(String aPath);

	@Transactional
	@Modifying
	@Query("DELETE FROM SongFile s WHERE s.updateDate < ?1")
	public void deleteUpdatedBefore(Date aDate);

}
