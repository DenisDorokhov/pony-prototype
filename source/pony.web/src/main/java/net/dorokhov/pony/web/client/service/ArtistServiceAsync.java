package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.ArrayList;

public interface ArtistServiceAsync {

	public void getCount(AsyncCallback<Long> aCallback);

	public void getAll(AsyncCallback<ArrayList<ArtistDto>> aCallback);

	public void search(String aQuery, AsyncCallback<ArrayList<ArtistDto>> aCallback);

	public void getById(Integer aId, AsyncCallback<ArtistDto> aCallback);
	public void getByName(String aName, AsyncCallback<ArtistDto> aCallback);
	public void getByIdOrName(String aNameOrId, AsyncCallback<ArtistDto> aCallback);

}
