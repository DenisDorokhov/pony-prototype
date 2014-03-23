package net.dorokhov.pony.web.server.service.impl;

import net.dorokhov.pony.core.exception.ConcurrentScanException;
import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.web.server.service.LibraryServiceFacade;
import net.dorokhov.pony.web.server.utility.DtoConverter;
import net.dorokhov.pony.web.shared.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class LibraryServiceFacadeImpl implements LibraryServiceFacade {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private LibraryScanner libraryScanner;

	private String libraryPath;

	@Autowired
	public void setLibraryScanner(LibraryScanner aLibraryScanner) {
		libraryScanner = aLibraryScanner;
	}

	@Value("${library.path}")
	public void setLibraryPath(String aLibraryPath) {
		libraryPath = aLibraryPath;
	}

	@Override
	synchronized public void startScanning() throws ConcurrentScanException {

		if (libraryScanner.getStatus() != null) {
			throw new ConcurrentScanException();
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					libraryScanner.scan(new File(libraryPath));
				} catch (Exception e) {
					log.error("could not scan library", e);
				}
			}
		}).start();
	}

	@Override
	public StatusDto getStatus() {

		LibraryScanner.Status status = libraryScanner.getStatus();

		return status != null ? DtoConverter.statusToDto(status) : null;
	}
}
