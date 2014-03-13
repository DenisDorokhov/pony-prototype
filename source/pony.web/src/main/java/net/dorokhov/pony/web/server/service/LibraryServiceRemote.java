package net.dorokhov.pony.web.server.service;

import net.dorokhov.pony.core.exception.ConcurrentScanException;
import net.dorokhov.pony.web.shared.StatusDto;

public interface LibraryServiceRemote {

	public void startScanning() throws ConcurrentScanException;

	public StatusDto getStatus();

}
