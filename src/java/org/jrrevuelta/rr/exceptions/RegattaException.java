package org.jrrevuelta.rr.exceptions;

public class RegattaException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	
	public RegattaException() {
		super();
	}
	
	public RegattaException(String message) {
		super(message);
	}
	
	public RegattaException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
