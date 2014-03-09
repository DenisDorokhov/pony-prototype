package net.dorokhov.pony.web.domain.response;

public abstract class AbstractResponse {

	private boolean successful;

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean aSuccessful) {
		successful = aSuccessful;
	}
}
