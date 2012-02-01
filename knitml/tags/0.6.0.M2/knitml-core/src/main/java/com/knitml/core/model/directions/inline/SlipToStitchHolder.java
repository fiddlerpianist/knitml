package com.knitml.core.model.directions.inline;

import com.knitml.core.model.directions.DiscreteInlineOperation;
import com.knitml.core.model.header.StitchHolder;

public class SlipToStitchHolder implements DiscreteInlineOperation {

	protected Integer numberOfStitches;
	protected StitchHolder stitchHolder;

	public SlipToStitchHolder() {
	}

	public SlipToStitchHolder(int numberOfStitches) {
		this.numberOfStitches = numberOfStitches;
	}
	
	public Integer getNumberOfStitches() {
		return numberOfStitches;
	}
	
	public StitchHolder getStitchHolder() {
		return stitchHolder;
	}

	public int getAdvanceCount() {
		return numberOfStitches;
	}

	public int getIncreaseCount() {
		return 0 - numberOfStitches;
	}

}
