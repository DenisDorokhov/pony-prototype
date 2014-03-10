package net.dorokhov.pony.core.exception;

public class ConcurrentScanException extends Exception {

	public ConcurrentScanException() {
		super("Library is already scanning.");
	}
}
