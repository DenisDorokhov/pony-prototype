package net.dorokhov.pony.web.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class StatusDto implements Serializable {

	private ArrayList<String> targetFiles;

	private String description;

	private double progress;

	private int step;

	private int totalSteps;

	public ArrayList<String> getTargetFiles() {

		if (targetFiles == null) {
			targetFiles = new ArrayList<String>();
		}

		return targetFiles;
	}

	public void setTargetFiles(ArrayList<String> aTargetFiles) {
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
