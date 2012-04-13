package com.knitml.core.model.common;


public class StitchesOnNeedle {

	protected Needle needle;
	protected Integer numberOfStitches;

	public StitchesOnNeedle() {
	}
	
	public StitchesOnNeedle(Needle needle, int numberOfStitches) {
		this.needle = needle;
		this.numberOfStitches = numberOfStitches;
	}

	public Integer getNumberOfStitches() {
		return numberOfStitches;
	}

	public Needle getNeedle() {
		return needle;
	}

}
