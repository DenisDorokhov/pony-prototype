package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;

@RemoteServiceRelativePath("rpc/songService")
public interface SongService extends RemoteService {

	public Long getCountByAlbum(Integer aAlbumId);
	public Long getCountByArtist(Integer aArtistId);

	public ArrayList<SongDto> getByAlbum(Integer aAlbumId);
	public ArrayList<SongDto> getByArtist(Integer aArtistId);

	public ArrayList<SongDto> search(String aQuery);

	public SongDto getById(Integer aId);

}
