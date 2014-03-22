package net.dorokhov.pony.web.server.service;

import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.List;

public interface ArtistServiceFacade {

	public Long getCount();

	public List<ArtistDto> getAll();

	public List<ArtistDto> search(String aQuery);

	public ArtistDto getById(Integer aId);
	public ArtistDto getByName(String aName);
	public ArtistDto getByIdOrName(String aNameOrId);

}
