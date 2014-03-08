package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.InstallationDao;
import net.dorokhov.pony.core.domain.Installation;
import net.dorokhov.pony.core.service.InstallationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstallationServiceImpl implements InstallationService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private InstallationDao installationDao;

	@Autowired
	public void setInstallationDao(InstallationDao aInstallationDao) {
		installationDao = aInstallationDao;
	}

	@Override
	@Transactional(readOnly = true)
	public Installation getInstallation() {
		return installationDao.findInstallation();
	}

	@Override
	@Transactional
	public void install() {

		log.info("Installing...");

		if (getInstallation() != null) {
			throw new RuntimeException("Already installed.");
		}

		installationDao.install();

		log.info("Successfully installed.");
	}

	@Override
	@Transactional
	public void uninstall() {

		log.info("Uninstalling...");

		if (getInstallation() == null) {
			throw new RuntimeException("Not installed.");
		}

		installationDao.uninstall();

		log.info("Successfully uninstalled.");
	}
}
