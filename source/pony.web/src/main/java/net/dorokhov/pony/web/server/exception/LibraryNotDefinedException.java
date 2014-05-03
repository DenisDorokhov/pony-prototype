package net.dorokhov.pony.web.server.exception;

public class LibraryNotDefinedException extends RuntimeException {

	public LibraryNotDefinedException() {
		super("No library files defined.");
	}
}
