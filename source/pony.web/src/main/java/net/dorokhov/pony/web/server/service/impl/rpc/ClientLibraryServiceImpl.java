package net.dorokhov.pony.web.server.service.impl.rpc;

import net.dorokhov.pony.core.exception.ConcurrentScanException;
import net.dorokhov.pony.web.client.service.ClientLibraryService;
import net.dorokhov.pony.web.server.service.LibraryServiceRemote;
import net.dorokhov.pony.web.shared.StatusDto;
import org.springframework.context.ApplicationContext;

public class ClientLibraryServiceImpl extends AbstractClientService implements ClientLibraryService {

	private LibraryServiceRemote libraryService;

	@Override
	protected void initWithApplicationContext(ApplicationContext aContext) {
		libraryService = aContext.getBean(LibraryServiceRemote.class);
	}

	@Override
	public boolean startScanning() {

		try {

			libraryService.startScanning();

			return true;

		} catch (ConcurrentScanException e) {
			log.error("library is already being scanned");
		}

		return false;
	}

	@Override
	public StatusDto getStatus() {
		return libraryService.getStatus();
	}

}
