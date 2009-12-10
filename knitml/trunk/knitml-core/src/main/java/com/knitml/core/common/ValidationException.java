package com.knitml.core.common;

public class ValidationException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private Object objectInError;

	public Object getObjectInError() {
		return objectInError;
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(String message, Object objectInError) {
		super(message);
		this.objectInError = objectInError;
	}
}
