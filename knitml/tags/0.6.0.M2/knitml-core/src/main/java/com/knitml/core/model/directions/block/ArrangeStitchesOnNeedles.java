package com.knitml.core.model.directions.block;

import java.util.List;

import com.knitml.core.common.StitchesOnNeedle;
import com.knitml.core.model.directions.BlockOperation;

public class ArrangeStitchesOnNeedles implements BlockOperation {
	
	protected List<StitchesOnNeedle> needles;

	public List<StitchesOnNeedle> getNeedles() {
		return needles;
	}

}
