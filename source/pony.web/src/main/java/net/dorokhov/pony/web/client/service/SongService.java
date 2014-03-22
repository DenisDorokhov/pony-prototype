package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.List;

@RemoteServiceRelativePath("rpc/songService")
public interface SongService extends RemoteService {

	public Long getCountByAlbum(Integer aAlbumId);
	public Long getCountByArtist(Integer aArtistId);

	public List<SongDto> getByAlbum(Integer aAlbumId);
	public List<SongDto> getByArtist(Integer aArtistId);

	public List<SongDto> search(String aText);

	public SongDto getById(Integer aId);

}
