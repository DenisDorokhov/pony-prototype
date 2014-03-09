package net.dorokhov.pony.web.service.impl;

import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.web.domain.StatusDto;
import net.dorokhov.pony.web.service.LibraryServiceRemote;
import net.dorokhov.pony.web.utility.DtoUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class LibraryServiceRemoteImpl implements LibraryServiceRemote {

	private static final String LIBRARY_PATH = "/Volumes/Volume_1/Shared/Music/Denis"; // TODO: configure library path

	private final Logger log = LoggerFactory.getLogger(getClass());

	private LibraryScanner libraryScanner;

	@Autowired
	public void setLibraryScanner(LibraryScanner aLibraryScanner) {
		libraryScanner = aLibraryScanner;
	}

	@Override
	synchronized public void startScanning() {

		if (libraryScanner.getStatus().isScanning()) {
			throw new RuntimeException("Concurrent scan.");
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					libraryScanner.scan(new File(LIBRARY_PATH));
				} catch (Exception e) {
					log.error("could not scan library", e);
				}
			}
		}).start();
	}

	@Override
	public StatusDto getStatus() {
		return DtoUtility.statusToDto(libraryScanner.getStatus());
	}
}
