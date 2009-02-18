package com.knitml.core.model.directions.inline;

import com.knitml.core.model.directions.DiscreteInlineOperation;

public class NoStitch implements DiscreteInlineOperation {

	protected Integer numberOfStitches;

	public Integer getNumberOfStitches() {
		return numberOfStitches;
	}
	
	public NoStitch() {
	}

	public NoStitch(Integer numberOfStitches) {
		this.numberOfStitches = numberOfStitches;
	}

	public int getAdvanceCount() {
		return 0;
	}
	
	public int getIncreaseCount() {
		return 0;
	}
	
}
