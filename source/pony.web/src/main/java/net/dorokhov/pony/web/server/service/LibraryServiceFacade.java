package net.dorokhov.pony.web.server.service;

import net.dorokhov.pony.web.shared.StatusDto;
import net.dorokhov.pony.web.shared.exception.ConcurrentScanException;
import net.dorokhov.pony.web.shared.exception.LibraryNotDefinedException;

public interface LibraryServiceFacade {

	public void startScanning() throws ConcurrentScanException, LibraryNotDefinedException;

	public StatusDto getStatus();

}
