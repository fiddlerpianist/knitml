package com.knitml.renderer.chart.writer.impl;

import com.knitml.renderer.common.RenderingException;

public class CannotRenderSymbolException extends RenderingException {

	private static final long serialVersionUID = 1L;

	public CannotRenderSymbolException() {
		super();
	}

	public CannotRenderSymbolException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotRenderSymbolException(String message) {
		super(message);
	}

	public CannotRenderSymbolException(Throwable cause) {
		super(cause);
	}

}
