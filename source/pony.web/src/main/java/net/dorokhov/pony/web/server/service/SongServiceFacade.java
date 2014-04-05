package net.dorokhov.pony.web.server.service;

import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;

public interface SongServiceFacade {

	public Long getCountByAlbum(Integer aAlbumId);
	public Long getCountByArtist(Integer aArtistId);

	public ArrayList<SongDto> getByAlbum(Integer aAlbumId);
	public ArrayList<SongDto> getByArtist(Integer aArtistId);

	public ArrayList<SongDto> search(String aQuery);

	public SongDto getById(Integer aId);

}
