package com.knitml.engine.common;

public class NotEndOfRowException extends KnittingEngineException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4684339256072913056L;

	public NotEndOfRowException() {
		super();
	}

	public NotEndOfRowException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotEndOfRowException(String message) {
		super(message);
	}

	public NotEndOfRowException(Throwable cause) {
		super(cause);
	}

}
