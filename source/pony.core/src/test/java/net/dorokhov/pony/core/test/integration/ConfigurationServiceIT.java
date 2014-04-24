package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.domain.Configuration;
import net.dorokhov.pony.core.service.ConfigurationService;
import net.dorokhov.pony.core.test.AbstractIntegrationCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolationException;
import java.util.Date;

public class ConfigurationServiceIT extends AbstractIntegrationCase {

	private ConfigurationService service;

	@Before
	public void setUp() throws Exception {
		service = context.getBean(ConfigurationService.class);
	}

	@Test
	public void testCrud() throws Exception {

		doTestSavingAndReading();
		doTestTypes();

		Assert.assertEquals(5, service.getCount());

		service.deleteById("booleanTest");

		Assert.assertNull(service.getById("booleanTest"));

		Assert.assertEquals(4, service.getAll().size());
	}

	@Test
	public void testValidation() {

		Configuration config = new Configuration();

		config.setId(null);
		config.setValue(null);

		boolean isExceptionThrown = false;

		try {
			service.validate(config);
		} catch (ConstraintViolationException e) {

			isExceptionThrown = true;

			Assert.assertEquals(1, e.getConstraintViolations().size()); // null id
		}

		Assert.assertTrue(isExceptionThrown);
	}

	private void doTestSavingAndReading() throws Exception {

		Configuration config = buildConfig();

		config = service.save(config);

		checkCreatedConfig(config);

		config = service.getById("stringTest");

		checkCreatedConfig(config);

		Date lastCreationDate = config.getCreationDate();
		Date lastUpdateDate = config.getUpdateDate();

		Thread.sleep(1000);

		config.setValue("stringValueChanged");

		config = service.save(config);

		checkUpdatedConfig(config, lastCreationDate, lastUpdateDate);

		config = service.getById("stringTest");

		checkUpdatedConfig(config, lastCreationDate, lastUpdateDate);
	}

	private void doTestTypes() {

		Configuration config = new Configuration();

		config.setId("longTest");
		config.setLong(10L);

		service.save(config);

		Assert.assertEquals(10L, service.getById("longTest").getLong());

		config = new Configuration();

		config.setId("intTest");
		config.setInteger(100);

		service.save(config);

		Assert.assertEquals(100, service.getById("intTest").getInteger());

		config = new Configuration();

		config.setId("doubleTest");
		config.setDouble(1.1);

		service.save(config);

		Assert.assertEquals(1.1, service.getById("doubleTest").getDouble(), 0.01);

		config = new Configuration();

		config.setId("booleanTest");
		config.setBoolean(true);

		service.save(config);

		Assert.assertTrue(service.getById("booleanTest").getBoolean());
	}

	private Configuration buildConfig() {

		Configuration config = new Configuration();

		config.setId("stringTest");
		config.setValue("stringValue");

		return config;
	}

	private void checkCreatedConfig(Configuration aEntity) {

		Assert.assertEquals("stringTest", aEntity.getId());
		Assert.assertNotNull(aEntity.getVersion());

		Assert.assertNotNull(aEntity.getCreationDate());
		Assert.assertNotNull(aEntity.getUpdateDate());

		Assert.assertEquals("stringValue", aEntity.getValue());
	}

	private void checkUpdatedConfig(Configuration aEntity, Date aCreationDate, Date aUpdateDate) {

		Assert.assertEquals("stringTest", aEntity.getId());
		Assert.assertNotNull(aEntity.getVersion());

		Assert.assertEquals(aCreationDate, aEntity.getCreationDate());
		Assert.assertTrue(aEntity.getUpdateDate().after(aUpdateDate));

		Assert.assertEquals("stringValueChanged", aEntity.getValue());
	}

}
