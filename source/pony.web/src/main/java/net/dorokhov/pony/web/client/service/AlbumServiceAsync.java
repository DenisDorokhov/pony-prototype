package net.dorokhov.pony.web.client.service;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.AlbumDto;
import net.dorokhov.pony.web.shared.AlbumSongsDto;

import java.util.ArrayList;

public interface AlbumServiceAsync {

	public Request getCountByArtist(Integer aArtistId, AsyncCallback<Long> aCallback);

	public Request getByArtist(Integer aArtistId, AsyncCallback<ArrayList<AlbumSongsDto>> aCallback);
	public Request getByArtistIdOrName(String aIdOrName, AsyncCallback<ArrayList<AlbumSongsDto>> aCallback);

	public Request search(String aQuery, AsyncCallback<ArrayList<AlbumDto>> aCallback);

	public Request getById(Integer aId, AsyncCallback<AlbumSongsDto> aCallback);

}
