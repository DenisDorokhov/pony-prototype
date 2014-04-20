package net.dorokhov.pony.web.server.service;

import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.ArrayList;

public interface ArtistServiceFacade {

	public Long getCount();

	public ArrayList<ArtistDto> getAll();

	public ArrayList<ArtistDto> search(String aQuery);

	public ArtistDto getById(Long aId);
	public ArtistDto getByName(String aName);
	public ArtistDto getByIdOrName(String aNameOrId);

}
