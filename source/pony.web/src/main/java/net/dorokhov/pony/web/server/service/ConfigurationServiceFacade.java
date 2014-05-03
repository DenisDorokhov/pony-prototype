package net.dorokhov.pony.web.server.service;

import net.dorokhov.pony.web.shared.ConfigurationDto;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface ConfigurationServiceFacade {

	public List<ConfigurationDto> getAll();

	public List<ConfigurationDto> save(List<ConfigurationDto> aConfigurations) throws ConstraintViolationException;

}
