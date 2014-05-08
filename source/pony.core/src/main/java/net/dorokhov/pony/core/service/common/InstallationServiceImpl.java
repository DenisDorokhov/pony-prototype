package net.dorokhov.pony.core.service.common;

import net.dorokhov.pony.core.dao.InstallationDao;
import net.dorokhov.pony.core.domain.Installation;
import net.dorokhov.pony.core.exception.AlreadyInstalledException;
import net.dorokhov.pony.core.exception.NotInstalledException;
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
	public Installation install() throws AlreadyInstalledException {

		log.info("Installing...");

		if (getInstallation() != null) {
			throw new AlreadyInstalledException();
		}

		Installation installation = installationDao.install();

		log.info("Successfully installed.");

		return installation;
	}

	@Override
	@Transactional
	public void uninstall() throws NotInstalledException {

		log.info("Uninstalling...");

		if (getInstallation() == null) {
			throw new NotInstalledException();
		}

		installationDao.uninstall();

		log.info("Successfully uninstalled.");
	}
}
