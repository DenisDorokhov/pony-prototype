package net.dorokhov.pony.core.exception;

import java.io.Serializable;

/**
 * Concurrent scanning exception,
 *
 * Signals that the system is already scanning the library.
 */
public class ConcurrentScanException extends RuntimeException implements Serializable {

	public ConcurrentScanException() {
		super("Library is already scanning.");
	}
}
