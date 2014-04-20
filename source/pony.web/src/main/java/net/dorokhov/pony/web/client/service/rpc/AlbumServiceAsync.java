package net.dorokhov.pony.web.client.service.rpc;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.AlbumDto;
import net.dorokhov.pony.web.shared.AlbumSongsDto;

import java.util.ArrayList;

public interface AlbumServiceAsync {

	public Request getCountByArtist(Long aArtistId, AsyncCallback<Long> aCallback);

	public Request getByArtist(Long aArtistId, AsyncCallback<ArrayList<AlbumSongsDto>> aCallback);
	public Request getByArtistIdOrName(String aIdOrName, AsyncCallback<ArrayList<AlbumSongsDto>> aCallback);

	public Request search(String aQuery, AsyncCallback<ArrayList<AlbumDto>> aCallback);

	public Request getById(Long aId, AsyncCallback<AlbumSongsDto> aCallback);

}
