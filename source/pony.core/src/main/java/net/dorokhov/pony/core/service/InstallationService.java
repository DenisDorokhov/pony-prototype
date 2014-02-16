package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.entity.Installation;

public interface InstallationService {

	public Installation getInstallation();

	public void install();

	public void uninstall();

}
