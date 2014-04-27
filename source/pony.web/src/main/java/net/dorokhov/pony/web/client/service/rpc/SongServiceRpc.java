package net.dorokhov.pony.web.client.service.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;

@RemoteServiceRelativePath("rpc/songService")
public interface SongServiceRpc extends RemoteService {

	public Long getCountByAlbum(Long aAlbumId);
	public Long getCountByArtist(Long aArtistId);

	public ArrayList<SongDto> getByAlbum(Long aAlbumId);
	public ArrayList<SongDto> getByArtist(Long aArtistId);

	public ArrayList<SongDto> search(String aQuery);

	public SongDto getById(Long aId);

}
