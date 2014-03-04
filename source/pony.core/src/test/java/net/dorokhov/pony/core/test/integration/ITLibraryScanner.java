package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.core.test.AbstractIntegrationCase;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ITLibraryScanner extends AbstractIntegrationCase {

	private LibraryScanner service;

	@Before
	public void setUp() throws Exception {
		service = context.getBean(LibraryScanner.class);
	}

	@Test
	public void testScan() {

		for (int i = 0; i < 3; i++) {

			LibraryScanner.Result result = service.scan(new File("/Volumes/Volume_1/Shared/Music/Denis/Slayer"));

			assertTrue(result.getScannedFoldersCount() > 0);
			assertTrue(result.getScannedFilesCount() > 0);
		}
	}

}
