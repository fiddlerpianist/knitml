package com.knitml.transform.gauge.event;

public class CannotTransformGaugeException extends Exception {

	private static final long serialVersionUID = 1L;

	public CannotTransformGaugeException() {
		super();
	}

	public CannotTransformGaugeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CannotTransformGaugeException(String arg0) {
		super(arg0);
	}

	public CannotTransformGaugeException(Throwable arg0) {
		super(arg0);
	}

}
