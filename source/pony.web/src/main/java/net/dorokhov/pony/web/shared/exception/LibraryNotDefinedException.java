package net.dorokhov.pony.web.shared.exception;

import java.io.Serializable;

public class LibraryNotDefinedException extends Exception implements Serializable {

	public LibraryNotDefinedException() {
		super("No library files defined.");
	}
}
