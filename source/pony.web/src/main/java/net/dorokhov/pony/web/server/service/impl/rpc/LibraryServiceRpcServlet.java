package net.dorokhov.pony.web.server.service.impl.rpc;

import net.dorokhov.pony.web.client.service.rpc.LibraryServiceRpc;
import net.dorokhov.pony.web.server.service.LibraryServiceFacade;
import net.dorokhov.pony.web.shared.StatusDto;
import net.dorokhov.pony.web.shared.exception.ConcurrentScanException;
import net.dorokhov.pony.web.shared.exception.LibraryNotDefinedException;
import org.springframework.web.context.WebApplicationContext;

public class LibraryServiceRpcServlet extends AbstractServiceRpcServlet implements LibraryServiceRpc {

	private LibraryServiceFacade libraryServiceFacade;

	@Override
	protected void initWithApplicationContext(WebApplicationContext aContext) {
		libraryServiceFacade = aContext.getBean(LibraryServiceFacade.class);
	}

	@Override
	public void startScanning() throws ConcurrentScanException, LibraryNotDefinedException {
		libraryServiceFacade.startScanning();
	}

	@Override
	public StatusDto getStatus() {
		return libraryServiceFacade.getStatus();
	}

}
