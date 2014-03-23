package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.Installation;

/**
 * Installation DAO.
 */
public interface InstallationDao {

	/**
	 * Finds system installation.
	 *
	 * @return system installation or {@literal null} if the system is not installed
	 */
	public Installation findInstallation();

	/**
	 * Installs the system.
	 */
	public void install();

	/**
	 * Uninstalls the system.
	 */
	public void uninstall();

}
