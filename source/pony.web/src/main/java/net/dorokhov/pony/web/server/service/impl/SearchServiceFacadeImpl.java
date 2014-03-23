package net.dorokhov.pony.web.server.service.impl;

import net.dorokhov.pony.web.server.service.AlbumServiceFacade;
import net.dorokhov.pony.web.server.service.ArtistServiceFacade;
import net.dorokhov.pony.web.server.service.SearchServiceFacade;
import net.dorokhov.pony.web.server.service.SongServiceFacade;
import net.dorokhov.pony.web.shared.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceFacadeImpl implements SearchServiceFacade {

	private ArtistServiceFacade artistServiceFacade;

	private AlbumServiceFacade albumServiceFacade;

	private SongServiceFacade songServiceFacade;

	@Autowired
	public void setArtistServiceFacade(ArtistServiceFacade aArtistServiceFacade) {
		artistServiceFacade = aArtistServiceFacade;
	}

	@Autowired
	public void setAlbumServiceFacade(AlbumServiceFacade aAlbumServiceFacade) {
		albumServiceFacade = aAlbumServiceFacade;
	}

	@Autowired
	public void setSongServiceFacade(SongServiceFacade aSongServiceFacade) {
		songServiceFacade = aSongServiceFacade;
	}

	@Override
	public SearchDto search(String aQuery) {

		SearchDto dto = new SearchDto();

		dto.setArtists(artistServiceFacade.search(aQuery));
		dto.setAlbums(albumServiceFacade.search(aQuery));
		dto.setSongs(songServiceFacade.search(aQuery));

		return dto;
	}

}
