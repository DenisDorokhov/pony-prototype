package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import net.dorokhov.pony.web.shared.SearchDto;

@RemoteServiceRelativePath("rpc/searchService")
public interface SearchService extends RemoteService {

	public SearchDto search(String aQuery);

}
