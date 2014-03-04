package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.ArtistDao;
import net.dorokhov.pony.core.entity.Artist;
import net.dorokhov.pony.core.service.ArtistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ArtistServiceImpl extends AbstractEntityService<Artist, ArtistDao> implements ArtistService {

	@Override
	@Transactional(readOnly = true)
	public Artist getByName(String aName) {
		return dao.findByName(aName.trim());
	}

	@Override
	@Transactional
	public void deleteUpdatedBefore(Date aDate) {
		dao.deleteUpdatedBefore(aDate);
	}

	@Override
	protected void normalize(Artist aArtist) {
		if (aArtist.getName() != null) {
			aArtist.setName(aArtist.getName().trim());
		}
	}

}
