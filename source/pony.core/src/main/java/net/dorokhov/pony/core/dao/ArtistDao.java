package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArtistDao extends PagingAndSortingRepository<Artist, Integer> {

	public Page<Artist> findByArtworkId(Integer aStoredFileId, Pageable aPageable);

	public Artist findByName(String aName);

}
