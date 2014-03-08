package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.ArtistDao;
import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.service.ArtistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArtistServiceImpl extends AbstractEntityService<Artist, ArtistDao> implements ArtistService {

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
