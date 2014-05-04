package net.dorokhov.pony.web.server.service.impl;

import net.dorokhov.pony.core.domain.Configuration;
import net.dorokhov.pony.core.service.ConfigurationService;
import net.dorokhov.pony.web.server.service.ConfigurationServiceFacade;
import net.dorokhov.pony.web.server.service.DtoService;
import net.dorokhov.pony.web.shared.ConfigurationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigurationServiceFacadeImpl implements ConfigurationServiceFacade {

	private ConfigurationService configurationService;

	private DtoService dtoService;

	private TransactionTemplate transactionTemplate;

	@Autowired
	public void setConfigurationService(ConfigurationService aConfigurationService) {
		configurationService = aConfigurationService;
	}

	@Autowired
	public void setDtoService(DtoService aDtoService) {
		dtoService = aDtoService;
	}

	@Autowired
	public void setTransactionManager(PlatformTransactionManager aTransactionManager) {
		transactionTemplate = new TransactionTemplate(aTransactionManager);
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
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<ConfigurationDto> save(final List<ConfigurationDto> aConfigurations) throws ConstraintViolationException {

		// Run new transaction to get updated entity versions in DTOs
		List<Configuration> savedConfiguration = transactionTemplate.execute(new TransactionCallback<List<Configuration>>() {
			@Override
			public List<Configuration> doInTransaction(TransactionStatus status) {

				List<Configuration> result = new ArrayList<Configuration>();

				for (ConfigurationDto dto : aConfigurations) {
					result.add(configurationService.save(dtoService.dtoToConfiguration(dto)));
				}

				return result;
			}
		});

		List<ConfigurationDto> result = new ArrayList<ConfigurationDto>();

		for (Configuration config : savedConfiguration) {
			result.add(dtoService.configurationToDto(config));
		}

		return result;
	}
}