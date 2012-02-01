package com.knitml.engine.common;

public class NeedlesInWrongDirectionException extends KnittingEngineException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7558548173812003965L;

	public NeedlesInWrongDirectionException() {
	}

	public NeedlesInWrongDirectionException(String message) {
		super(message);
	}

	public NeedlesInWrongDirectionException(Throwable cause) {
		super(cause);
	}

	public NeedlesInWrongDirectionException(String message, Throwable cause) {
		super(message, cause);
	}

}
