package net.dorokhov.pony.web.domain.response;

public class StatusResponse extends AbstractResponse {

	private boolean scanning;

	private double progress;

	public boolean isScanning() {
		return scanning;
	}

	public void setScanning(boolean aScanning) {
		scanning = aScanning;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double aProgress) {
		progress = aProgress;
	}
}
