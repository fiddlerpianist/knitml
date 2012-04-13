package com.knitml.renderer.common;

public class RenderingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RenderingException() {
		super();
	}

	public RenderingException(String message, Throwable cause) {
		super(message, cause);
	}

	public RenderingException(String message) {
		super(message);
	}

	public RenderingException(Throwable cause) {
		super(cause);
	}

}
