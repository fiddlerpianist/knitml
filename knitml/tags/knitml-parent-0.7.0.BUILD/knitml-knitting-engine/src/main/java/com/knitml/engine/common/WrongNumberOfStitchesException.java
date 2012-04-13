package com.knitml.engine.common;

import java.text.MessageFormat;


public class WrongNumberOfStitchesException extends KnittingEngineException {
	
	private static final long serialVersionUID = 1L;
	
	private int expected;
	private int actual;

	public WrongNumberOfStitchesException(int expected, int actual) {
		this.expected = expected;
		this.actual = actual;
	}

	@Override
	public String getMessage() {
		return MessageFormat.format(Messages.getString("EXPECTED_VS_ACTUAL"), expected, actual); //$NON-NLS-1$
	}
	
	

}
