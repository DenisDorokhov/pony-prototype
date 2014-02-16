package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.entity.Installation;

public interface InstallationDao {

	public Installation findInstallation();

	public void install();

	public void uninstall();

}
