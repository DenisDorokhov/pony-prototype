package net.dorokhov.pony.core.test;

import net.dorokhov.pony.core.service.InstallationService;
import net.dorokhov.pony.core.service.SearchService;
import net.dorokhov.pony.core.service.StoredFileService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AbstractIntegrationCase extends AbstractCase {

	protected ApplicationContext context;

	protected InstallationService installationService;

	protected StoredFileService storedFileService;

	protected SearchService searchService;

	public void baseSetUp() throws Exception {

		super.baseSetUp();

		context = new ClassPathXmlApplicationContext("context.xml");

		installationService = context.getBean(InstallationService.class);
		storedFileService = context.getBean(StoredFileService.class);
		searchService = context.getBean(SearchService.class);

		if (installationService.getInstallation() != null) {
			storedFileService.deleteAll();
			installationService.uninstall();
			searchService.clearIndex();
		}

		installationService.install();
	}

	public void baseTearDown() throws Exception {

		super.baseTearDown();

		if (installationService.getInstallation() != null) {
			storedFileService.deleteAll();
			installationService.uninstall();
			searchService.clearIndex();
		}
	}
}
