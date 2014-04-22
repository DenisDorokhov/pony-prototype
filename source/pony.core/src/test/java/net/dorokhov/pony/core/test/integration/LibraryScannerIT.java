package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.core.test.AbstractIntegrationCase;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.DecimalFormat;
import java.text.Format;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LibraryScannerIT extends AbstractIntegrationCase {

	private final Format progressFormatter = new DecimalFormat("###.##");

	private LibraryScanner service;

	private boolean didCallStart;
	private boolean didCallFinish;
	private boolean didCallFail;

	@Before
	public void setUp() throws Exception {

		didCallStart = false;
		didCallFinish = false;
		didCallFail = false;

		service = context.getBean(LibraryScanner.class);

		service.addDelegate(new LibraryScanner.Delegate() {

			@Override
			public void onScanStart() {
				didCallStart = true;
			}

			@Override
			public void onScanProgress(LibraryScanner.Status aStatus) {
				log.info("library scanner did progress {}%", progressFormatter.format(aStatus.getProgress() * 100.0));
			}

			@Override
			public void onScanFinish(LibraryScanner.Result aResult) {
				didCallFinish = true;
			}

			@Override
			public void onScanFail(Exception e) {
				didCallFail = true;
			}
		});
	}

	@Test
	public void testScan() throws Exception {

		for (int i = 0; i < 2; i++) {

			// TODO: change path to test MP3 files stored in VCS

			LibraryScanner.Result result = service.scan(new File("/Volumes/Volume_1/Shared/Music/Denis/Dio"));

			assertTrue(result.getScannedFoldersCount() > 0);
			assertTrue(result.getScannedFilesCount() > 0);
			assertTrue(result.getDuration() > 0);

			assertTrue(didCallStart);
			assertTrue(didCallFinish);
			assertFalse(didCallFail);
		}
	}

}
