package net.dorokhov.pony.web.client.service.rpc;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;

public interface SongServiceRpcAsync {

	public Request getCountByAlbum(Long aAlbumId, AsyncCallback<Long> aCallback);
	public Request getCountByArtist(Long aArtistId, AsyncCallback<Long> aCallback);

	public Request getByAlbum(Long aAlbumId, AsyncCallback<ArrayList<SongDto>> aCallback);
	public Request getByArtist(Long aArtistId, AsyncCallback<ArrayList<SongDto>> aCallback);

	public Request search(String aQuery, AsyncCallback<ArrayList<SongDto>> aCallback);

	public Request getById(Long aId, AsyncCallback<SongDto> aCallback);

}
