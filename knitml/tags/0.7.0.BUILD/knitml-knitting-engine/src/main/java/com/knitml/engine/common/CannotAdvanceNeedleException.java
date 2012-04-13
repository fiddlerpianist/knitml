package com.knitml.engine.common;


public class CannotAdvanceNeedleException extends KnittingEngineException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8083087903471637870L;

	public CannotAdvanceNeedleException() {
	}

	public CannotAdvanceNeedleException(String message) {
		super(message);
	}

	public CannotAdvanceNeedleException(Throwable cause) {
		super(cause);
	}

	public CannotAdvanceNeedleException(String message, Throwable cause) {
		super(message, cause);
	}

}
