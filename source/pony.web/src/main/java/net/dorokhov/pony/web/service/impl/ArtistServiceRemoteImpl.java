package net.dorokhov.pony.web.service.impl;

import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.service.ArtistService;
import net.dorokhov.pony.web.domain.ArtistDto;
import net.dorokhov.pony.web.service.ArtistServiceRemote;
import net.dorokhov.pony.web.utility.DtoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArtistServiceRemoteImpl implements ArtistServiceRemote {

	private ArtistService artistService;

	@Autowired
	public void setArtistService(ArtistService aArtistService) {
		artistService = aArtistService;
	}

	@Override
	public Long getCount() {
		return artistService.getCount();
	}

	@Override
	public List<ArtistDto> getAll() {

		List<ArtistDto> artistList = new ArrayList<ArtistDto>();

		for (Artist artist : artistService.getAll()) {
			artistList.add(DtoUtility.artistToDto(artist));
		}

		return artistList;
	}

	@Override
	public ArtistDto getById(Integer aId) {

		Artist artist = artistService.getById(aId);

		return artist != null ? DtoUtility.artistToDto(artist) : null;
	}
}
