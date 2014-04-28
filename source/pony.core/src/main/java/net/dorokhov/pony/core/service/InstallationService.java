package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.Installation;
import net.dorokhov.pony.core.exception.AlreadyInstalledException;
import net.dorokhov.pony.core.exception.NotInstalledException;

/**
 * Installation service.
 */
public interface InstallationService {

	/**
	 * Gets system installation.
	 *
	 * @return system installation or null if the system is not installed
	 */
	public Installation getInstallation();

	/**
	 * Installs the system.
	 */
	public void install() throws AlreadyInstalledException;

	/**
	 * Uninstalls the system.
	 */
	public void uninstall() throws NotInstalledException;

}
