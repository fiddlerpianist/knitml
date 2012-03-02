package com.knitml.core.model.operations.inline;

import com.knitml.core.model.operations.DiscreteInlineOperation;

public class PassPreviousStitchOver implements DiscreteInlineOperation {

	protected Integer numberOfTimes;

	public PassPreviousStitchOver() {
	}

	public PassPreviousStitchOver(int numberOfStitches) {
		this.numberOfTimes = numberOfStitches;
	}
	
	public Integer getNumberOfTimes() {
		return numberOfTimes;
	}
	
	public int getAdvanceCount() {
		return 0;
	}

	public int getIncreaseCount() {
		return 0 - numberOfTimes;
	}

}
