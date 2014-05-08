package net.dorokhov.pony.web.server.service;

import net.dorokhov.pony.core.exception.AlreadyInstalledException;
import net.dorokhov.pony.core.exception.NotInstalledException;
import net.dorokhov.pony.web.shared.InstallationDto;

public interface InstallationServiceFacade {

	public InstallationDto getInstallation();

	public InstallationDto install() throws AlreadyInstalledException;

	public void uninstall() throws NotInstalledException;

}
