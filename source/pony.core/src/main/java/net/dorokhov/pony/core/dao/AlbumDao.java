package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.entity.Album;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface AlbumDao extends PagingAndSortingRepository<Album, Integer> {

	public long countByArtistId(Integer aArtistId);

	public List<Album> findByArtistId(Integer aArtistId);

	public Album findByArtistIdAndName(Integer aArtistId, String aName);

	@Transactional
	@Modifying
	@Query("DELETE FROM Album a WHERE a.updateDate < ?1")
	public void deleteUpdatedBefore(Date aDate);

}
