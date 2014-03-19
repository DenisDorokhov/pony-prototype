package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.AlbumDao;
import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.service.AlbumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlbumServiceImpl extends AbstractEntityService<Album, Integer, AlbumDao> implements AlbumService {

	@Override
	@Transactional(readOnly = true)
	public long getCountByArtist(Integer aArtistId) {
		return dao.countByArtistId(aArtistId);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Album> getByArtwork(Integer aStoredFileId, Pageable aPageable) {
		return dao.findByArtworkId(aStoredFileId, aPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Album> getByArtist(Integer aArtistId) {
		return dao.findByArtistId(aArtistId, new Sort("year", "name"));
	}

	@Override
	@Transactional(readOnly = true)
	public Album getByArtistAndName(Integer aArtistId, String aName) {
		return dao.findByArtistIdAndName(aArtistId, aName.trim());
	}

	@Override
	protected void normalize(Album aAlbum) {
		if (aAlbum.getName() != null) {
			aAlbum.setName(aAlbum.getName().trim());
		}
	}
}
