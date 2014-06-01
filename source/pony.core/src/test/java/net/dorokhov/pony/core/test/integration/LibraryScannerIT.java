package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.domain.ScanResult;
import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.core.test.AbstractIntegrationCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

public class LibraryScannerIT extends AbstractIntegrationCase {

	private static final String TEST_FOLDER_PATH = "data/library";

	private final Format progressFormatter = new DecimalFormat("###.##");

	private LibraryScanner service;

	private LibraryScanner.Delegate delegate;

	private boolean didCallStart;
	private boolean didCallFinish;
	private boolean didCallFail;

	@Before
	public void setUp() throws Exception {

		didCallStart = false;
		didCallFinish = false;
		didCallFail = false;

		service = context.getBean(LibraryScanner.class);

		delegate = new LibraryScanner.Delegate() {

			@Override
			public void onScanStart() {
				didCallStart = true;
			}

			@Override
			public void onScanProgress(LibraryScanner.Status aStatus) {
				log.info("library scanner did progress {}%", progressFormatter.format(aStatus.getProgress() * 100.0));
			}

			@Override
			public void onScanFinish(ScanResult aResult) {
				didCallFinish = true;
			}

			@Override
			public void onScanFail(ScanResult aResult, Exception e) {
				didCallFail = true;
			}
		};

		service.addDelegate(delegate);
	}

	@After
	public void tearDown() throws Exception {
		service.removeDelegate(delegate);
	}

	@Test
	public void testScan() throws Exception {

		for (int i = 0; i <= 1; i++) {

			List<File> filesToScan = new ArrayList<File>();

			filesToScan.add(new ClassPathResource(TEST_FOLDER_PATH).getFile());

			ScanResult result = service.scan(filesToScan);

			Assert.assertNotNull(result.getId());
			Assert.assertNotNull(result.getDate());

			Assert.assertTrue(result.getSuccess());

			Assert.assertEquals(1, result.getTargetFiles().size());
			Assert.assertEquals(filesToScan.get(0).getAbsolutePath(), result.getTargetFiles().get(0));

			Assert.assertTrue(result.getDuration() > 0);

			Assert.assertEquals(Long.valueOf(1L), result.getScannedFolderCount());
			Assert.assertEquals(Long.valueOf(14L), result.getScannedFileCount());

			Assert.assertTrue(i > 0 || result.getImportedFileCount() == 14);

			Assert.assertTrue(didCallStart);
			Assert.assertTrue(didCallFinish);
			Assert.assertFalse(didCallFail);
		}

		ScanResult result = service.getLastResult();

		Assert.assertNotNull(result);
	}

}
