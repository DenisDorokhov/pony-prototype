package net.dorokhov.pony.core.exception;

import java.io.Serializable;

public class ConcurrentScanException extends Exception implements Serializable {

	public ConcurrentScanException() {
		super("Library is already scanning.");
	}

}
