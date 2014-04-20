package net.dorokhov.pony.web.client.service.rpc;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.SearchDto;

public interface SearchServiceAsync {

	public Request search(String aQuery, AsyncCallback<SearchDto> aCallback);

}
