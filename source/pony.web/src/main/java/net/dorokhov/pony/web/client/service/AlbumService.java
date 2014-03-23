package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import net.dorokhov.pony.web.shared.AlbumDto;
import net.dorokhov.pony.web.shared.AlbumSongsDto;

import java.util.List;

@RemoteServiceRelativePath("rpc/albumService")
public interface AlbumService extends RemoteService {

	public Long getCountByArtist(Integer aArtistId);

	public List<AlbumSongsDto> getByArtist(Integer aArtistId);
	public List<AlbumSongsDto> getByArtistIdOrName(String aIdOrName);

	public List<AlbumDto> search(String aQuery);

	public AlbumSongsDto getById(Integer aId);

}
