package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.Configuration;

import javax.validation.ConstraintViolationException;

public interface ConfigurationService {

	public Configuration getById(String aId);

	public Configuration save(Configuration aConfig) throws ConstraintViolationException;

	public void validate(Configuration aConfig) throws ConstraintViolationException;

}
