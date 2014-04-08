package net.dorokhov.pony.web.client.service;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;

public interface SongServiceAsync {

	public Request getCountByAlbum(Integer aAlbumId, AsyncCallback<Long> aCallback);
	public Request getCountByArtist(Integer aArtistId, AsyncCallback<Long> aCallback);

	public Request getByAlbum(Integer aAlbumId, AsyncCallback<ArrayList<SongDto>> aCallback);
	public Request getByArtist(Integer aArtistId, AsyncCallback<ArrayList<SongDto>> aCallback);

	public Request search(String aQuery, AsyncCallback<ArrayList<SongDto>> aCallback);

	public Request getById(Integer aId, AsyncCallback<SongDto> aCallback);

}
