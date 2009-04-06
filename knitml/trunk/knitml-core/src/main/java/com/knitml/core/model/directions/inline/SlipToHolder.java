package com.knitml.core.model.directions.inline;

import com.knitml.core.model.directions.DiscreteInlineOperation;
import com.knitml.core.model.header.StitchHolder;

public class SlipToHolder implements DiscreteInlineOperation {

	protected Integer numberOfStitches;
	protected StitchHolder stitchHolder;

	public SlipToHolder() {
	}

	public SlipToHolder(int numberOfStitches) {
		this.numberOfStitches = numberOfStitches;
	}
	
	public Integer getNumberOfStitches() {
		return numberOfStitches;
	}

	public int getAdvanceCount() {
		return numberOfStitches;
	}

	public int getIncreaseCount() {
		return 0 - numberOfStitches;
	}

}
