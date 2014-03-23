package net.dorokhov.pony.web.server.service;

import net.dorokhov.pony.web.shared.AlbumDto;
import net.dorokhov.pony.web.shared.AlbumSongsDto;

import java.util.List;

public interface AlbumServiceFacade {

	public Long getCountByArtist(Integer aArtistId);

	public List<AlbumSongsDto> getByArtist(Integer aArtistId);
	public List<AlbumSongsDto> getByArtistIdOrName(String aIdOrName);

	public List<AlbumDto> search(String aQuery);

	public AlbumSongsDto getById(Integer aId);

}
