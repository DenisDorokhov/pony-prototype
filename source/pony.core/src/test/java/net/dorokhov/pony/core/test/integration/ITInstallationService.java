package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.domain.Installation;
import net.dorokhov.pony.core.service.InstallationService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ITInstallationService {

	private InstallationService installationService;

	@Before
	public void setUp() throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");

		installationService = context.getBean(InstallationService.class);

		restore();
	}

	@After
	public void tearDown() throws Exception {
		restore();
	}

	@Test
	public void testInstallation() throws Exception {

		Assert.assertNull(installationService.getInstallation());

		installationService.install();

		Installation installation = installationService.getInstallation();

		Assert.assertNotNull(installation);
		Assert.assertNotNull(installation.getSystemVersion());

		installationService.uninstall();

		Assert.assertNull(installationService.getInstallation());
	}

	private void restore() throws Exception {
		if (installationService.getInstallation() != null) {
			installationService.uninstall();
		}
	}
}
