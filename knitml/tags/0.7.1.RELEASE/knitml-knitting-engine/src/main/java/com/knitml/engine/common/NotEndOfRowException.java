package com.knitml.engine.common;

public class NotEndOfRowException extends KnittingEngineException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4684339256072913056L;
	
	private Integer numberOfStitchesLeft;

	public Integer getNumberOfStitchesLeft() {
		return this.numberOfStitchesLeft;
	}

	public NotEndOfRowException(String message, int numberOfStitchesLeft) {
		super(message);
		this.numberOfStitchesLeft = numberOfStitchesLeft;
	}

	public NotEndOfRowException(String message, int numberOfStitchesLeft, Throwable cause) {
		super(message, cause);
		this.numberOfStitchesLeft = numberOfStitchesLeft;
	}
	public NotEndOfRowException(Throwable cause) {
		super(cause);
	}

}
