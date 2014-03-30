package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.Installation;

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
	public void install();

	/**
	 * Uninstalls the system.
	 */
	public void uninstall();

}
