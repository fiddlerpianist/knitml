package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.common.StitchHolder;
import com.knitml.core.model.operations.DiscreteInlineOperation;

public class SlipToStitchHolder implements DiscreteInlineOperation {

	protected Integer numberOfStitches;
	protected StitchHolder stitchHolder;

	public SlipToStitchHolder() {
	}

	public SlipToStitchHolder(int numberOfStitches) {
		this.numberOfStitches = numberOfStitches;
	}
	
	public List<SlipToStitchHolder> canonicalize() {
		int size = numberOfStitches == null ? 1 : numberOfStitches;
		List<SlipToStitchHolder> newOps = new ArrayList<SlipToStitchHolder>(size);
		for (int i = 0; i < size; i++) {
			newOps.add(new SlipToStitchHolder(1));
		}
		return newOps;
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
