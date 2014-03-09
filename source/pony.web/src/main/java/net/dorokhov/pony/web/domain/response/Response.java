package net.dorokhov.pony.web.domain.response;

public class Response {

	private final boolean successful;

	public Response(boolean aSuccessful) {
		successful = aSuccessful;
	}

	public boolean isSuccessful() {
		return successful;
	}
}
