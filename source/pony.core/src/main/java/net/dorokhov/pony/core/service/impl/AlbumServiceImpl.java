package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.AlbumDao;
import net.dorokhov.pony.core.entity.Album;
import net.dorokhov.pony.core.service.AlbumService;

import java.util.List;

public class AlbumServiceImpl extends AbstractEntityService<Album, AlbumDao> implements AlbumService {

	@Override
	public Long getCountByArtist(Integer aArtistId) {
		return dao.countByArtistId(aArtistId);
	}

	@Override
	public List<Album> getByArtist(Integer aArtistId) {
		return dao.findByArtistId(aArtistId);
	}

	@Override
	public Album getByArtistAndName(Integer aArtistId, String aName) {
		return dao.findByArtistIdAndName(aArtistId, aName);
	}
}
