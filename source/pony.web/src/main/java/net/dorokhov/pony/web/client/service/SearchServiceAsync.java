package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.SearchDto;

public interface SearchServiceAsync {

	public void search(String aQuery, AsyncCallback<SearchDto> aCallback);

}
