package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.AlbumDto;

import java.util.List;

public interface ClientAlbumServiceAsync {

	public void getCountByArtist(Integer aArtistId, AsyncCallback<Long> aCallback);

	public void getByArtist(Integer aArtistId, AsyncCallback<List<AlbumDto>> aCallback);
	public void getByArtistIdOrName(String aIdOrName, AsyncCallback<List<AlbumDto>> aCallback);

	public void getById(Integer aId, AsyncCallback<AlbumDto> aCallback);

}
