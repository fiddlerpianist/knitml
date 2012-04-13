package com.knitml.engine.common;


public class CannotPutMarkerOnEndOfNeedleException extends KnittingEngineException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 656649330345687943L;

	public CannotPutMarkerOnEndOfNeedleException() {
	}

	public CannotPutMarkerOnEndOfNeedleException(String message) {
		super(message);
	}

	public CannotPutMarkerOnEndOfNeedleException(Throwable cause) {
		super(cause);
	}

	public CannotPutMarkerOnEndOfNeedleException(String message, Throwable cause) {
		super(message, cause);
	}

}
