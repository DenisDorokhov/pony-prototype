package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.Configuration;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Configuration service.
 */
public interface ConfigurationService {

	/**
	 * Gets number of configuration options.
	 *
	 * @return number of configuration options
	 */
	public long getCount();

	/**
	 * Gets all configuration options.
	 *
	 * @return list of all configuration options
	 */
	public List<Configuration> getAll();

	/**
	 * Gets configuration by ID.
	 *
	 * @param aId configuration ID
	 * @return configuration with the given ID or null if none found
	 */
	public Configuration getById(String aId);

	/**
	 * Saves configuration.
	 *
	 * @param aConfig configuration to save
	 * @return saved configuration
	 * @throws ConstraintViolationException in case configuration is not valid
	 */
	public Configuration save(Configuration aConfig) throws ConstraintViolationException;

	/**
	 * Deletes configuration by ID.
	 *
	 * @param aId configuration ID
	 */
	public void deleteById(String aId);

	/**
	 * Validates configuration.
	 *
	 * @param aConfig configuration to validate
	 * @throws ConstraintViolationException in case configuration is not valid
	 */
	public void validate(Configuration aConfig) throws ConstraintViolationException;

}
