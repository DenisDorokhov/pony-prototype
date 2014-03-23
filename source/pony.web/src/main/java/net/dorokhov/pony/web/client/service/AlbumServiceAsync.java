package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.AlbumDto;
import net.dorokhov.pony.web.shared.AlbumSongsDto;

import java.util.List;

public interface AlbumServiceAsync {

	public void getCountByArtist(Integer aArtistId, AsyncCallback<Long> aCallback);

	public void getByArtist(Integer aArtistId, AsyncCallback<List<AlbumSongsDto>> aCallback);
	public void getByArtistIdOrName(String aIdOrName, AsyncCallback<List<AlbumSongsDto>> aCallback);

	public void search(String aQuery, AsyncCallback<List<AlbumDto>> aCallback);

	public void getById(Integer aId, AsyncCallback<AlbumSongsDto> aCallback);

}
