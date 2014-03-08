package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.core.test.AbstractIntegrationCase;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.DecimalFormat;
import java.text.Format;

import static org.junit.Assert.*;

public class ITLibraryScanner extends AbstractIntegrationCase {

	private final Format progressFormatter = new DecimalFormat("###.##");

	private LibraryScanner service;

	private boolean didCallStart;
	private double lastProgress;
	private boolean didCallFinish;

	@Before
	public void setUp() throws Exception {

		didCallStart = false;
		lastProgress = 0.0;
		didCallFinish = false;

		service = context.getBean(LibraryScanner.class);

		service.addDelegate(new LibraryScanner.Delegate() {

			@Override
			public void onScanStart() {
				didCallStart = true;
			}

			@Override
			public void onScanProgress(double aProgress) {

				assertTrue(aProgress >= lastProgress);

				log.info("library scanner did progress {}%", progressFormatter.format(aProgress * 100.0));
			}

			@Override
			public void onScanFinish(LibraryScanner.Result aResult) {
				didCallFinish = true;
			}
		});
	}

	@Test
	public void testScan() {

		for (int i = 0; i < 2; i++) {

			LibraryScanner.Result result = service.scan(new File("/Volumes/Volume_1/Shared/Music/Denis/Slayer"));

			assertTrue(result.getScannedFoldersCount() > 0);
			assertTrue(result.getScannedFilesCount() > 0);
			assertTrue(result.getDuration() > 0);

			assertTrue(didCallStart);
			assertTrue(didCallFinish);
		}
	}

}
