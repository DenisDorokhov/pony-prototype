package net.dorokhov.pony.web.client.service.rpc;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.ArrayList;

public interface ArtistServiceAsync {

	public Request getCount(AsyncCallback<Long> aCallback);

	public Request getAll(AsyncCallback<ArrayList<ArtistDto>> aCallback);

	public Request search(String aQuery, AsyncCallback<ArrayList<ArtistDto>> aCallback);

	public Request getById(Long aId, AsyncCallback<ArtistDto> aCallback);
	public Request getByName(String aName, AsyncCallback<ArtistDto> aCallback);
	public Request getByIdOrName(String aNameOrId, AsyncCallback<ArtistDto> aCallback);

}
