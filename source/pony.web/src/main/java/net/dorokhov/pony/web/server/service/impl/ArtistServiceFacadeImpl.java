package net.dorokhov.pony.web.server.service.impl;

import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.service.ArtistService;
import net.dorokhov.pony.web.server.service.ArtistServiceFacade;
import net.dorokhov.pony.web.shared.ArtistDto;
import net.dorokhov.pony.web.server.utility.DtoUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArtistServiceFacadeImpl implements ArtistServiceFacade {

	private ArtistService artistService;

	@Autowired
	public void setArtistService(ArtistService aArtistService) {
		artistService = aArtistService;
	}

	@Override
	@Transactional(readOnly = true)
	public Long getCount() {
		return artistService.getCount();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArtistDto> getAll() {
		return artistListToDto(artistService.getAll());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArtistDto> search(String aQuery) {
		return artistListToDto(artistService.search(aQuery));
	}

	@Override
	@Transactional(readOnly = true)
	public ArtistDto getById(Integer aId) {

		Artist artist = artistService.getById(aId);

		return artist != null ? DtoUtility.artistToDto(artist) : null;
	}

	@Override
	@Transactional(readOnly = true)
	public ArtistDto getByName(String aName) {

		Artist artist = artistService.getByName(aName);

		return artist != null ? DtoUtility.artistToDto(artist) : null;
	}

	@Override
	@Transactional(readOnly = true)
	public ArtistDto getByIdOrName(String aIdOrName) {

		ArtistDto artist = null;

		if (StringUtils.isNumeric(aIdOrName)) {
			artist = getById(NumberUtils.toInt(aIdOrName));
		}

		if (artist == null) {
			artist = getByName(aIdOrName);
		}

		return artist;
	}

	private List<ArtistDto> artistListToDto(List<Artist> aArtistList) {

		List<ArtistDto> dto = new ArrayList<ArtistDto>();

		for (Artist artist : aArtistList) {
			dto.add(DtoUtility.artistToDto(artist));
		}

		return dto;
	}
}
