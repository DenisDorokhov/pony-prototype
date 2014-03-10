package net.dorokhov.pony.web.service;

import net.dorokhov.pony.core.exception.ConcurrentScanException;
import net.dorokhov.pony.web.domain.StatusDto;

public interface LibraryServiceRemote {

	public void startScanning() throws ConcurrentScanException;

	public StatusDto getStatus();
}
