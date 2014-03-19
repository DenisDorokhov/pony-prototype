package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.Artist;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ArtistDao extends PagingAndSortingRepository<Artist, Integer> {

	public List<Artist> findByArtworkId(Integer aStoredFileId, Sort aSort);

	public Artist findByName(String aName);

}
