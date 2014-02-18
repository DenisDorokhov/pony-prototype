package net.dorokhov.pony.core.test;

import net.dorokhov.pony.core.service.InstallationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AbstractIntegrationCase extends AbstractCase {

	protected ApplicationContext context;

	protected InstallationService installationService;

	public void baseSetUp() throws Exception {

		super.baseSetUp();

		context = new ClassPathXmlApplicationContext("context.xml");

		installationService = context.getBean(InstallationService.class);

		if (installationService.getInstallation() != null) {
			installationService.uninstall();
		}

		installationService.install();
	}

	public void baseTearDown() throws Exception {

		super.baseTearDown();

		if (installationService.getInstallation() != null) {
			installationService.uninstall();
		}
	}
}
