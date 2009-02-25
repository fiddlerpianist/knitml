package com.knitml.engine.common;

public class NotEnoughStitchesException extends KnittingEngineException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7224850450185150749L;

	public NotEnoughStitchesException() {
		super();
	}

	public NotEnoughStitchesException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotEnoughStitchesException(String message) {
		super(message);
	}

	public NotEnoughStitchesException(Throwable cause) {
		super(cause);
	}

}
