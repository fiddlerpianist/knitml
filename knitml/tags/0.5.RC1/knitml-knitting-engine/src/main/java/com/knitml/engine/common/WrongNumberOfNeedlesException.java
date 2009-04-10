package com.knitml.engine.common;


public class WrongNumberOfNeedlesException extends KnittingEngineException {
	
	private static final long serialVersionUID = 1L;
	
	private int expected;
	private int actual;

	public WrongNumberOfNeedlesException(int expected, int actual) {
		this.expected = expected;
		this.actual = actual;
	}

	@Override
	public String getMessage() {
		return "Expected " + expected + " needles, but found " + actual;
	}
	
	

}
