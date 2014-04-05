package net.dorokhov.pony.web.server.service;

import net.dorokhov.pony.web.shared.AlbumDto;
import net.dorokhov.pony.web.shared.AlbumSongsDto;

import java.util.ArrayList;

public interface AlbumServiceFacade {

	public Long getCountByArtist(Integer aArtistId);

	public ArrayList<AlbumSongsDto> getByArtist(Integer aArtistId);
	public ArrayList<AlbumSongsDto> getByArtistIdOrName(String aIdOrName);

	public ArrayList<AlbumDto> search(String aQuery);

	public AlbumSongsDto getById(Integer aId);

}
