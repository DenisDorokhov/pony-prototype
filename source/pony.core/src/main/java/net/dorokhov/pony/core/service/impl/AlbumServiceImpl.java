package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.AlbumDao;
import net.dorokhov.pony.core.entity.Album;
import net.dorokhov.pony.core.service.AlbumService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlbumServiceImpl extends AbstractEntityService<Album, AlbumDao> implements AlbumService {

	@Override
	@Transactional(readOnly = true)
	public Long getCountByArtist(Integer aArtistId) {
		return dao.countByArtistId(aArtistId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Album> getByArtist(Integer aArtistId) {
		return dao.findByArtistId(aArtistId);
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
