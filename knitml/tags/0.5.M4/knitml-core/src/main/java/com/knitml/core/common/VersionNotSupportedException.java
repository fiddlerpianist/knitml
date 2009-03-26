package com.knitml.core.common;

public class VersionNotSupportedException extends RuntimeException {

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
