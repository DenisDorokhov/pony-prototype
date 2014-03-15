package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.Album;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AlbumDao extends PagingAndSortingRepository<Album, Integer> {

	public long countByArtistId(Integer aArtistId);

	public List<Album> findByArtistId(Integer aArtistId, Sort aSort);

	public Album findByArtistIdAndName(Integer aArtistId, String aName);

}
