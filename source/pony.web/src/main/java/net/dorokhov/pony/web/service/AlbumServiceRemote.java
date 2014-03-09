package net.dorokhov.pony.web.service;

import net.dorokhov.pony.web.domain.AlbumDto;

import java.util.List;

public interface AlbumServiceRemote {

	public Long getCountByArtist(Integer aArtistId);

	public List<AlbumDto> getByArtist(Integer aArtistId);

	public AlbumDto getById(Integer aId);

}
