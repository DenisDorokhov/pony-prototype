package net.dorokhov.pony.web.service;

import net.dorokhov.pony.web.domain.ArtistDto;

import java.util.List;

public interface ArtistServiceRemote {

	public Long getCount();

	public List<ArtistDto> getAll();

	public ArtistDto getById(Integer aId);

}
