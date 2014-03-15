package net.dorokhov.pony.web.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatusDto implements Serializable {

	private boolean scanning;

	private List<String> targetFiles;

	private String description;

	private double progress;

	public boolean isScanning() {
		return scanning;
	}

	public void setScanning(boolean aScanning) {
		scanning = aScanning;
	}

	public List<String> getTargetFiles() {

		if (targetFiles == null) {
			targetFiles = new ArrayList<String>();
		}

		return targetFiles;
	}

	public void setTargetFiles(List<String> aTargetFiles) {
		targetFiles = aTargetFiles;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String aDescription) {
		description = aDescription;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double aProgress) {
		progress = aProgress;
	}
}
