package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.List;

public interface SongServiceAsync {

	public void getCountByAlbum(Integer aAlbumId, AsyncCallback<Long> aCallback);
	public void getCountByArtist(Integer aArtistId, AsyncCallback<Long> aCallback);

	public void getByAlbum(Integer aAlbumId, AsyncCallback<List<SongDto>> aCallback);
	public void getByArtist(Integer aArtistId, AsyncCallback<List<SongDto>> aCallback);

	public void search(String aQuery, AsyncCallback<List<SongDto>> aCallback);

	public void getById(Integer aId, AsyncCallback<SongDto> aCallback);

}
