package org.jrrevuelta.rr.service.exceptions;

public class TeamException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	
	public TeamException() {
		super();
	}
	
	public TeamException(String message) {
		super(message);
	}
	
	public TeamException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
