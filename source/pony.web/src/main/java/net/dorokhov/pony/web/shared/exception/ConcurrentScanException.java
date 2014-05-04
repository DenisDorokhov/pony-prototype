package net.dorokhov.pony.web.shared.exception;

import java.io.Serializable;

public class ConcurrentScanException extends Exception implements Serializable {

	public ConcurrentScanException() {
		super("Library is already scanning.");
	}
}
