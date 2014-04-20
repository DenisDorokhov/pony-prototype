package net.dorokhov.pony.web.server.service;

import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;

public interface SongServiceFacade {

	public Long getCountByAlbum(Long aAlbumId);
	public Long getCountByArtist(Long aArtistId);

	public ArrayList<SongDto> getByAlbum(Long aAlbumId);
	public ArrayList<SongDto> getByArtist(Long aArtistId);

	public ArrayList<SongDto> search(String aQuery);

	public SongDto getById(Long aId);

}
