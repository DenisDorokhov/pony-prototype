package net.dorokhov.pony.web.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatusDto implements Serializable {

	private List<String> targetFiles;

	private String description;

	private double progress;

	private int step;

	private int totalSteps;

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

	public int getStep() {
		return step;
	}

	public void setStep(int aStep) {
		step = aStep;
	}

	public int getTotalSteps() {
		return totalSteps;
	}

	public void setTotalSteps(int aTotalSteps) {
		totalSteps = aTotalSteps;
	}
}
