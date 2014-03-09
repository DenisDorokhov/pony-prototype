package net.dorokhov.pony.web.service;

import net.dorokhov.pony.web.domain.SongDto;

import java.util.List;

public interface SongServiceRemote {

	public Long getCountByAlbum(Integer aAlbumId);
	public Long getCountByArtist(Integer aArtistId);

	public List<SongDto> getByAlbum(Integer aAlbumId);
	public List<SongDto> getByArtist(Integer aArtistId);

	public SongDto getById(Integer aId);

}
