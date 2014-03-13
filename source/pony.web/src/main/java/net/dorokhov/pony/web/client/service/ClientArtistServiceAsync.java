package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.List;

public interface ClientArtistServiceAsync {

	public void getCount(AsyncCallback<Long> aCallback);

	public void getAll(AsyncCallback<List<ArtistDto>> aCallback);

	public void getById(AsyncCallback<ArtistDto> aCallback);
	public void getByName(AsyncCallback<ArtistDto> aCallback);
	public void getByIdOrName(AsyncCallback<ArtistDto> aCallback);

}
