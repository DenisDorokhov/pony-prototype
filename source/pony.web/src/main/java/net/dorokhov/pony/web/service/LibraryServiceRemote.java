package net.dorokhov.pony.web.service;

import net.dorokhov.pony.web.domain.StatusDto;

public interface LibraryServiceRemote {

	public void startScanning();

	public StatusDto getStatus();
}
