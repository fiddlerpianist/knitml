package com.knitml.engine.common;


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
		return "Expected " + expected + " stitches, but found " + actual;
	}
	
	

}
