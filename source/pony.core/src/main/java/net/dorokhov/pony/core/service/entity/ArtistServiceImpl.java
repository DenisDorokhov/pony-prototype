package net.dorokhov.pony.core.service.entity;

import net.dorokhov.pony.core.dao.ArtistDao;
import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.service.ArtistService;
import net.dorokhov.pony.core.service.SearchService;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class ArtistServiceImpl extends AbstractEntityService<Artist, Integer, ArtistDao> implements ArtistService {

	private static final int MAX_SEARCH_RESULTS = 10;

	private SearchService searchService;

	@Autowired
	public void setSearchService(SearchService aSearchService) {
		searchService = aSearchService;
	}

	@Override
	@Transactional(readOnly = true)
	public long getCountByArtwork(Integer aStoredFileId) {
		return dao.countByArtworkId(aStoredFileId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Artist> getAll() {

		List<Artist> result = IteratorUtils.toList(dao.findAll(new Sort("name")).iterator());

		Collections.sort(result);

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Artist> getByArtwork(Integer aStoredFileId) {

		List<Artist> result = dao.findByArtworkId(aStoredFileId, new Sort("name"));

		Collections.sort(result);

		return result;
	}

	@Override
	public List<Artist> search(String aQuery) {
		return searchService.searchArtists(aQuery, MAX_SEARCH_RESULTS);
	}

	@Override
	@Transactional(readOnly = true)
	public Artist getByName(String aName) {
		return dao.findByName(aName.trim());
	}

	@Override
	protected void normalize(Artist aArtist) {
		if (aArtist.getName() != null) {
			aArtist.setName(aArtist.getName().trim());
		}
	}

}
