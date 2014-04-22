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

		Configuration config = new Configuration();

		config.setId("stringTest");
		config.setValue("stringValue");

		service.save(config);

		config = service.getById("stringTest");

		Assert.assertEquals("stringTest", config.getId());
		Assert.assertNotNull(config.getCreationDate());
		Assert.assertNotNull(config.getUpdateDate());
		Assert.assertEquals("stringValue", config.getValue());

		Date creationDate = config.getCreationDate();
		Date updateDate = config.getUpdateDate();

		Thread.sleep(1000);

		config.setValue("stringValueChanged");

		config = service.save(config);

		Assert.assertEquals(creationDate, config.getCreationDate());
		Assert.assertTrue(updateDate.before(config.getUpdateDate()));

		config = new Configuration();

		config.setId("longTest");
		config.setLong(10L);

		service.save(config);

		config = new Configuration();

		config.setId("intTest");
		config.setInteger(100);

		service.save(config);

		config = new Configuration();

		config.setId("doubleTest");
		config.setDouble(1.1);

		service.save(config);

		config = new Configuration();

		config.setId("booleanTest");
		config.setBoolean(true);

		service.save(config);

		Assert.assertEquals(5, service.getCount());
		Assert.assertEquals(5, service.getAll().size());

		service.deleteById("booleanTest");

		Assert.assertNull(service.getById("booleanTest"));
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

			Assert.assertEquals(1, e.getConstraintViolations().size());
		}

		Assert.assertTrue(isExceptionThrown);
	}

}
