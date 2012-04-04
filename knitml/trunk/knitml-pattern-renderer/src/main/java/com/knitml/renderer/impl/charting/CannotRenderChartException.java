package com.knitml.renderer.impl.charting;

import com.knitml.renderer.common.RenderingException;

public class CannotRenderChartException extends RenderingException {

	private static final long serialVersionUID = 1L;

	public CannotRenderChartException() {
	}

	public CannotRenderChartException(String message) {
		super(message);
	}

	public CannotRenderChartException(Throwable cause) {
		super(cause);
	}

	public CannotRenderChartException(String message, Throwable cause) {
		super(message, cause);
	}

}
