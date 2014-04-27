package net.dorokhov.pony.web.server.service;

import net.dorokhov.pony.web.shared.AlbumDto;
import net.dorokhov.pony.web.shared.AlbumSongsDto;

import java.util.ArrayList;

public interface AlbumServiceFacade {

	public Long getCountByArtist(Long aArtistId);

	public ArrayList<AlbumSongsDto> getByArtist(Long aArtistId);
	public ArrayList<AlbumSongsDto> getByArtistIdOrName(String aIdOrName);

	public ArrayList<AlbumDto> search(String aQuery);

	public AlbumSongsDto getById(Long aId);

}
