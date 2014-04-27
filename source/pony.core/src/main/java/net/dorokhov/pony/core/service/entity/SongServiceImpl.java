package net.dorokhov.pony.core.service.entity;

import net.dorokhov.pony.core.dao.SongDao;
import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.service.SearchService;
import net.dorokhov.pony.core.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class SongServiceImpl extends AbstractEntityService<Song, Long, SongDao> implements SongService {

	private static final int MAX_SEARCH_RESULTS = 10;

	private SearchService searchService;

	@Autowired
	public void setSearchService(SearchService aSearchService) {
		searchService = aSearchService;
	}

	@Override
	@Transactional(readOnly = true)
	public long getCountByAlbum(Long aAlbumId) {
		return dao.countByAlbumId(aAlbumId);
	}

	@Override
	@Transactional(readOnly = true)
	public long getCountByArtist(Long aArtistId) {
		return dao.countByAlbumArtistId(aArtistId);
	}

	@Override
	@Transactional(readOnly = true)
	public Song getById(Long aId) {
		return dao.findById(aId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Song> getByAlbum(Long aAlbumId) {

		List<Song> result = dao.findByAlbumId(aAlbumId, new Sort("file.discNumber", "file.trackNumber", "file.name"));

		Collections.sort(result);

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Song> getByArtist(Long aArtistId) {

		List<Song> result = dao.findByAlbumArtistId(aArtistId, new Sort("album.year", "album.name", "file.discNumber", "file.trackNumber", "file.name"));

		Collections.sort(result);

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Song> search(String aQuery) {
		return searchService.searchSongs(aQuery, MAX_SEARCH_RESULTS);
	}

	@Override
	@Transactional(readOnly = true)
	public Song getByFile(Long aSongFileId) {
		return dao.findByFileId(aSongFileId);
	}

	@Override
	@Transactional
	public void deleteByFileId(Long aSongFileId) {
		dao.deleteByFileId(aSongFileId);
	}
}
