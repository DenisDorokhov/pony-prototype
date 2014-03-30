package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.Installation;

/**
 * Installation DAO.
 */
public interface InstallationDao {

	/**
	 * Finds database installation.
	 *
	 * @return database installation or null if the database is not installed
	 */
	public Installation findInstallation();

	/**
	 * Installs the database.
	 */
	public void install();

	/**
	 * Uninstalls the database.
	 */
	public void uninstall();

}
