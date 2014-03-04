package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.entity.Artist;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface ArtistDao extends PagingAndSortingRepository<Artist, Integer> {

	public Artist findByName(String aName);

	@Transactional
	@Modifying
	@Query("DELETE FROM Artist a WHERE a.updateDate < ?1")
	public void deleteUpdatedBefore(Date aDate);

}
