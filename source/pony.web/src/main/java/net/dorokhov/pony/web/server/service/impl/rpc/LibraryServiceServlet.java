package net.dorokhov.pony.web.server.service.impl.rpc;

import net.dorokhov.pony.core.exception.ConcurrentScanException;
import net.dorokhov.pony.web.client.service.rpc.LibraryService;
import net.dorokhov.pony.web.server.service.LibraryServiceFacade;
import net.dorokhov.pony.web.shared.StatusDto;
import org.springframework.web.context.WebApplicationContext;

public class LibraryServiceServlet extends AbstractServiceServlet implements LibraryService {

	private LibraryServiceFacade libraryServiceFacade;

	@Override
	protected void initWithApplicationContext(WebApplicationContext aContext) {
		libraryServiceFacade = aContext.getBean(LibraryServiceFacade.class);
	}

	@Override
	public boolean startScanning() {

		try {

			libraryServiceFacade.startScanning();

			return true;

		} catch (ConcurrentScanException e) {
			log.error("library is already being scanned");
		}

		return false;
	}

	@Override
	public StatusDto getStatus() {
		return libraryServiceFacade.getStatus();
	}

}
