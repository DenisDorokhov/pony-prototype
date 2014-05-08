package net.dorokhov.pony.web.server.service.impl;

import net.dorokhov.pony.core.domain.Configuration;
import net.dorokhov.pony.core.domain.Installation;
import net.dorokhov.pony.core.exception.AlreadyInstalledException;
import net.dorokhov.pony.core.exception.NotInstalledException;
import net.dorokhov.pony.core.service.ConfigurationService;
import net.dorokhov.pony.core.service.InstallationService;
import net.dorokhov.pony.web.server.service.DtoService;
import net.dorokhov.pony.web.server.service.InstallationServiceFacade;
import net.dorokhov.pony.web.shared.ConfigurationOptions;
import net.dorokhov.pony.web.shared.InstallationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstallationServiceFacadeImpl implements InstallationServiceFacade {

	private static final int AUTO_SCAN_INTERVAL = 86400;

	private InstallationService installationService;

	private DtoService dtoService;

	private ConfigurationService configurationService;

	@Autowired
	public void setInstallationService(InstallationService aInstallationService) {
		installationService = aInstallationService;
	}

	@Autowired
	public void setDtoService(DtoService aDtoService) {
		dtoService = aDtoService;
	}

	@Autowired
	public void setConfigurationService(ConfigurationService aConfigurationService) {
		configurationService = aConfigurationService;
	}

	@Override
	@Transactional(readOnly = true)
	public InstallationDto getInstallation() {

		Installation installation = installationService.getInstallation();

		return installation != null ? dtoService.installationToDto(installation) : null;
	}

	@Override
	@Transactional
	public InstallationDto install() throws AlreadyInstalledException {

		InstallationDto installation = dtoService.installationToDto(installationService.install());

		Configuration config = new Configuration();

		config.setId(ConfigurationOptions.AUTO_SCAN_INTERVAL);
		config.setInteger(AUTO_SCAN_INTERVAL);

		configurationService.save(config);

		return installation;
	}

	@Override
	@Transactional
	public void uninstall() throws NotInstalledException {
		installationService.uninstall();
	}
}
