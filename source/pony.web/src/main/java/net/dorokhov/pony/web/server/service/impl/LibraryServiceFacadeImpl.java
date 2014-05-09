package net.dorokhov.pony.web.server.service.impl;

import net.dorokhov.pony.core.domain.Configuration;
import net.dorokhov.pony.core.domain.ScanResult;
import net.dorokhov.pony.core.service.ConfigurationService;
import net.dorokhov.pony.core.service.InstallationService;
import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.web.shared.ScanResultDto;
import net.dorokhov.pony.web.shared.ScanStatusDto;
import net.dorokhov.pony.web.shared.exception.ConcurrentScanException;
import net.dorokhov.pony.web.shared.exception.LibraryNotDefinedException;
import net.dorokhov.pony.web.server.service.DtoService;
import net.dorokhov.pony.web.server.service.LibraryServiceFacade;
import net.dorokhov.pony.web.shared.ConfigurationOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LibraryServiceFacadeImpl implements LibraryServiceFacade {

	private static final String LIBRARY_PATH_SEPARATOR = ";";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private LibraryScanner libraryScanner;

	private ConfigurationService configurationService;

	private DtoService dtoService;

	private InstallationService installationService;

	@Autowired
	public void setLibraryScanner(LibraryScanner aLibraryScanner) {
		libraryScanner = aLibraryScanner;
	}

	@Autowired
	public void setConfigurationService(ConfigurationService aConfigurationService) {
		configurationService = aConfigurationService;
	}

	@Autowired
	public void setDtoService(DtoService aDtoService) {
		dtoService = aDtoService;
	}

	@Autowired
	public void setInstallationService(InstallationService aInstallationService) {
		installationService = aInstallationService;
	}

	@Override
	synchronized public void startScanning() throws ConcurrentScanException, LibraryNotDefinedException {
		doStartScanning(getLibraryFiles());
	}

	@Override
	public ScanStatusDto getStatus() {

		LibraryScanner.Status status = libraryScanner.getStatus();

		return status != null ? dtoService.scanStatusToDto(status) : null;
	}

	@Override
	public ScanResultDto getLastResult() {

		ScanResult result = libraryScanner.getLastResult();

		return result != null ? dtoService.scanResultToDto(result) : null;
	}

	@Scheduled(fixedDelay = 5 * 60 * 1000)
	synchronized public void autoScanIfNeeded() throws ConcurrentScanException, LibraryNotDefinedException {

		if (installationService.getInstallation() != null) {

			log.debug("checking if automatic scan needed...");

			boolean shouldScan = false;

			List<File> libraryFiles = getLibraryFiles();

			if (libraryFiles.size() > 0) {

				if (libraryScanner.getStatus() == null) {

					Configuration config = configurationService.getById(ConfigurationOptions.AUTO_SCAN_INTERVAL);

					if (config != null && config.getValue() != null) {

						ScanResult lastResult = libraryScanner.getLastResult();

						if (lastResult != null) {

							long secondsSinceLastScan = (new Date().getTime() - lastResult.getDate().getTime()) / 1000;

							if (secondsSinceLastScan >= config.getLong()) {
								shouldScan = true;
							} else {
								log.debug("too early for automatic scan");
							}

						} else {

							log.debug("library was never scanned before");

							shouldScan = true;
						}

					} else {
						log.debug("automatic scan is off");
					}

				} else {
					log.debug("library is already being scanned");
				}

			} else {
				log.debug("no library files defined");
			}

			if (shouldScan) {

				log.info("starting automatic scan...");

				doStartScanning(libraryFiles);
			}
		}
	}

	private List<File> getLibraryFiles() {

		List<File> result = new ArrayList<File>();

		Configuration config = configurationService.getById(ConfigurationOptions.LIBRARY_FOLDERS);

		if (config != null && config.getValue() != null) {
			for (String path : config.getValue().split(LIBRARY_PATH_SEPARATOR)) {

				path = path.trim();

				if (path.length() > 0) {
					result.add(new File(path));
				}
			}
		}

		return result;
	}

	private void doStartScanning(final List<File> libraryFiles) throws ConcurrentScanException, LibraryNotDefinedException {

		if (libraryScanner.getStatus() != null) {
			throw new ConcurrentScanException();
		}

		if (libraryFiles.size() == 0) {
			throw new LibraryNotDefinedException();
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					libraryScanner.scan(libraryFiles);
				} catch (Exception e) {
					log.error("could not scan library", e);
				}
			}
		}).start();
	}
}
