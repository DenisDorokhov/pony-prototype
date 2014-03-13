package net.dorokhov.pony.web.server.service;

import net.dorokhov.pony.web.shared.AlbumDto;

import java.util.List;

public interface AlbumServiceRemote {

	public Long getCountByArtist(Integer aArtistId);

	public List<AlbumDto> getByArtist(Integer aArtistId);
	public List<AlbumDto> getByArtistIdOrName(String aIdOrName);

	public AlbumDto getById(Integer aId);

}
