package net.dorokhov.pony.core.service.entity;

import net.dorokhov.pony.core.dao.AlbumDao;
import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.service.AlbumService;
import net.dorokhov.pony.core.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class AlbumServiceImpl extends AbstractEntityService<Album, Long, AlbumDao> implements AlbumService {

	private static final int MAX_SEARCH_RESULTS = 10;

	private SearchService searchService;

	@Autowired
	public void setSearchService(SearchService aSearchService) {
		searchService = aSearchService;
	}

	@Override
	@Transactional(readOnly = true)
	public long getCountByArtist(Long aArtistId) {
		return dao.countByArtistId(aArtistId);
	}

	@Override
	@Transactional(readOnly = true)
	public long getCountByArtwork(Long aStoredFileId) {
		return dao.countByArtworkId(aStoredFileId);
	}

	@Override
	@Transactional(readOnly = true)
	public Album getById(Long aId) {
		return dao.findById(aId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Album> getByArtwork(Long aStoredFileId) {

		List<Album> result = dao.findByArtworkId(aStoredFileId, new Sort("artist", "year", "name"));

		Collections.sort(result);

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Album> getByArtist(Long aArtistId) {

		List<Album> result = dao.findByArtistId(aArtistId, new Sort("year", "name"));

		Collections.sort(result);

		return result;
	}

	@Override
	public List<Album> search(String aQuery) {
		return searchService.searchAlbums(aQuery, MAX_SEARCH_RESULTS);
	}

	@Override
	@Transactional(readOnly = true)
	public Album getByArtistAndName(Long aArtistId, String aName) {
		return dao.findByArtistIdAndName(aArtistId, aName.trim());
	}

	@Override
	protected void normalize(Album aAlbum) {
		if (aAlbum.getName() != null) {
			aAlbum.setName(aAlbum.getName().trim());
		}
	}
}
