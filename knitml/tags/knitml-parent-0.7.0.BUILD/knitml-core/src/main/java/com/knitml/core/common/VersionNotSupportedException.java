package com.knitml.core.common;

public class VersionNotSupportedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VersionNotSupportedException() {
		super();
	}

	public VersionNotSupportedException(String message, Throwable cause) {
		super(message, cause);
	}

	public VersionNotSupportedException(String message) {
		super(message);
	}

	public VersionNotSupportedException(Throwable cause) {
		super(cause);
	}

}
