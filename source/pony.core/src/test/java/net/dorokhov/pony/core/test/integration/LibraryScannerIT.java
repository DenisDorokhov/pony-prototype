package net.dorokhov.pony.core.test.integration;

import net.dorokhov.pony.core.domain.ScanResult;
import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.core.test.AbstractIntegrationCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

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
			public void onScanFinish(ScanResult aResult) {
				didCallFinish = true;
			}

			@Override
			public void onScanFail(ScanResult aResult, Exception e) {
				didCallFail = true;
			}
		});
	}

	@Test
	public void testScan() throws Exception {

		for (int i = 0; i < 2; i++) {

			// TODO: change path to test MP3 files stored in VCS

			List<File> filesToScan = new ArrayList<File>();

			filesToScan.add(new File("/Volumes/Volume_1/Shared/Music/Denis/Dio"));

			ScanResult result = service.scan(filesToScan);

			Assert.assertNotNull(result.getId());
			Assert.assertNotNull(result.getDate());

			Assert.assertTrue(result.getSuccess());

			Assert.assertTrue(result.getTargetFiles().size() == 1);
			Assert.assertEquals("/Volumes/Volume_1/Shared/Music/Denis/Dio", result.getTargetFiles().get(0));

			Assert.assertTrue(result.getDuration() > 0);

			Assert.assertTrue(result.getScannedFolderCount() > 0);
			Assert.assertTrue(result.getScannedFileCount() > 0);

			Assert.assertTrue(i > 0 || result.getImportedFileCount() > 0);

			Assert.assertTrue(didCallStart);
			Assert.assertTrue(didCallFinish);
			Assert.assertFalse(didCallFail);
		}

		ScanResult result = service.getLastResult();

		result.getTargetFiles();

		Assert.assertNotNull(result);
	}

}
