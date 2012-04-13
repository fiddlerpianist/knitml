package com.knitml.engine.common;

import java.text.MessageFormat;

import com.knitml.core.common.Side;

public class UnexpectedSideException extends KnittingEngineException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Side expected;
	private Side actual;

	public UnexpectedSideException(Side expected, Side actual) {
		this.expected = expected;
		this.actual = actual;
	}

	@Override
	public String getMessage() {
		return MessageFormat.format(Messages.getString("EXPECTED_VS_ACTUAL"), expected, actual); //$NON-NLS-1$
	}

}
