package net.dorokhov.pony.web.server.service.impl.rpc;

import net.dorokhov.pony.web.client.service.rpc.SearchServiceRpc;
import net.dorokhov.pony.web.server.service.SearchServiceFacade;
import net.dorokhov.pony.web.shared.SearchDto;
import org.springframework.web.context.WebApplicationContext;

public class SearchServiceRpcServlet extends AbstractServiceRpcServlet implements SearchServiceRpc {

	private SearchServiceFacade searchServiceFacade;

	@Override
	protected void initWithApplicationContext(WebApplicationContext aContext) {
		searchServiceFacade = aContext.getBean(SearchServiceFacade.class);
	}

	@Override
	public SearchDto search(String aQuery) {
		return searchServiceFacade.search(aQuery);
	}

}
