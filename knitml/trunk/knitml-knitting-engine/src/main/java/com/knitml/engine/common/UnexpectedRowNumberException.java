package com.knitml.engine.common;

public class UnexpectedRowNumberException extends KnittingEngineException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int expected;
	private int actual;

	public UnexpectedRowNumberException(int expected, int actual) {
		this.expected = expected;
		this.actual = actual;
	}

	@Override
	public String getMessage() {
		return "Expected " + expected + ", actual " + actual;
	}

}
