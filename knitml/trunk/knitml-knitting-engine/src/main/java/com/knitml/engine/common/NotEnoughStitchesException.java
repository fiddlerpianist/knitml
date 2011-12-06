package com.knitml.engine.common;

import java.text.MessageFormat;

public class NotEnoughStitchesException extends KnittingEngineException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7224850450185150749L;

	private int stitchesRequired;
	private int stitchesAvailable;

	public NotEnoughStitchesException(String message, int stitchesRequired,
			int stitchesAvailable) {
		super(message);
		this.stitchesRequired = stitchesRequired;
		this.stitchesAvailable = stitchesAvailable;
	}

	public NotEnoughStitchesException(int stitchesRequired,
			int stitchesAvailable) {
		super();
		this.stitchesRequired = stitchesRequired;
		this.stitchesAvailable = stitchesAvailable;
	}

	public int getStitchesRequired() {
		return this.stitchesRequired;
	}

	public int getStitchesAvailable() {
		return this.stitchesAvailable;
	}

	@Override
	public String getMessage() {
		return new StringBuilder(super.getMessage())
				.append("; ").append( //$NON-NLS-1$
				MessageFormat.format(
						Messages.getString("REQUIRED_VS_AVAILABLE"), //$NON-NLS-1$
						this.stitchesRequired, this.stitchesAvailable))
				.toString();
	}

}
