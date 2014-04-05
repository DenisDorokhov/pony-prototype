package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import net.dorokhov.pony.web.shared.AlbumDto;
import net.dorokhov.pony.web.shared.AlbumSongsDto;

import java.util.ArrayList;

@RemoteServiceRelativePath("rpc/albumService")
public interface AlbumService extends RemoteService {

	public Long getCountByArtist(Integer aArtistId);

	public ArrayList<AlbumSongsDto> getByArtist(Integer aArtistId);
	public ArrayList<AlbumSongsDto> getByArtistIdOrName(String aIdOrName);

	public ArrayList<AlbumDto> search(String aQuery);

	public AlbumSongsDto getById(Integer aId);

}
