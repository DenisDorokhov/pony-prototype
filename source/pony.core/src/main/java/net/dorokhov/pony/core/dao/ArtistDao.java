package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.entity.Artist;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArtistDao extends PagingAndSortingRepository<Artist, Integer> {

	public Artist findByName(String aName);

}
