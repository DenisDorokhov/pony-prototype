package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import net.dorokhov.pony.web.shared.AlbumDto;

import java.util.List;

@RemoteServiceRelativePath("rpc/albumService")
public interface AlbumService extends RemoteService {

	public Long getCountByArtist(Integer aArtistId);

	public List<AlbumDto> getByArtist(Integer aArtistId);
	public List<AlbumDto> getByArtistIdOrName(String aIdOrName);

	public List<AlbumDto> search(String aQuery);

	public AlbumDto getById(Integer aId);

}
