package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.ArtistDao;
import net.dorokhov.pony.core.entity.Artist;
import net.dorokhov.pony.core.service.ArtistService;

public class ArtistServiceImpl extends AbstractEntityService<Artist, ArtistDao> implements ArtistService {

	@Override
	public Artist getByName(String aName) {
		return dao.findByName(aName);
	}

}
