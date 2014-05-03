package net.dorokhov.pony.web.server.service.impl;

import net.dorokhov.pony.core.domain.Configuration;
import net.dorokhov.pony.core.service.ConfigurationService;
import net.dorokhov.pony.web.server.service.ConfigurationServiceFacade;
import net.dorokhov.pony.web.server.service.DtoService;
import net.dorokhov.pony.web.shared.ConfigurationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigurationServiceFacadeImpl implements ConfigurationServiceFacade {

	private ConfigurationService configurationService;

	private DtoService dtoService;

	@Autowired
	public void setConfigurationService(ConfigurationService aConfigurationService) {
		configurationService = aConfigurationService;
	}

	@Autowired
	public void setDtoService(DtoService aDtoService) {
		dtoService = aDtoService;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ConfigurationDto> getAll() {

		List<ConfigurationDto> result = new ArrayList<ConfigurationDto>();

		for (Configuration config : configurationService.getAll()) {
			result.add(dtoService.configurationToDto(config));
		}

		return result;
	}

	@Override
	@Transactional
	public List<ConfigurationDto> save(List<ConfigurationDto> aConfigurations) throws ConstraintViolationException {

		List<ConfigurationDto> result = new ArrayList<ConfigurationDto>();

		for (ConfigurationDto dto : aConfigurations) {

			Configuration config = configurationService.save(dtoService.dtoToConfiguration(dto));

			result.add(dtoService.configurationToDto(config));
		}

		return result;
	}
}
