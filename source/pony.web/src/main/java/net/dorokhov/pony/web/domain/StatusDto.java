package net.dorokhov.pony.web.domain;

import java.util.ArrayList;
import java.util.List;

public class StatusDto {

	private boolean scanning;

	private List<String> scanningFiles;

	private Double progress;

	public boolean isScanning() {
		return scanning;
	}

	public void setScanning(boolean aScanning) {
		scanning = aScanning;
	}

	public List<String> getScanningFiles() {

		if (scanningFiles == null) {
			scanningFiles = new ArrayList<String>();
		}

		return scanningFiles;
	}

	public void setScanningFiles(List<String> aScanningFiles) {
		scanningFiles = aScanningFiles;
	}

	public Double getProgress() {
		return progress;
	}

	public void setProgress(Double aProgress) {
		progress = aProgress;
	}
}
