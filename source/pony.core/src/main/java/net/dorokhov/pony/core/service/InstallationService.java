package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.Installation;

public interface InstallationService {

	public Installation getInstallation();

	public void install();

	public void uninstall();

}
