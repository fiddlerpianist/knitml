package com.knitml.engine.common;

public class NoMarkerFoundException extends KnittingEngineException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7554992339272625527L;

	public NoMarkerFoundException() {
	}

	public NoMarkerFoundException(String message) {
		super(message);
	}

	public NoMarkerFoundException(Throwable cause) {
		super(cause);
	}

	public NoMarkerFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
