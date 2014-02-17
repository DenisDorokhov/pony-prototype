package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.service.InstallationService;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AbstractIntegrationCase {

	protected ApplicationContext context;

	protected InstallationService installationService;

	@Before
	public void baseSetUp() throws Exception {

		context = new ClassPathXmlApplicationContext("context.xml");

		installationService = context.getBean(InstallationService.class);

		if (installationService.getInstallation() != null) {
			installationService.uninstall();
		}

		installationService.install();
	}

	@After
	public void baseTearDown() throws Exception {
		if (installationService.getInstallation() != null) {
			installationService.uninstall();
		}
	}
}
