package com.knitml.core.model.operations.block;

import java.util.List;

import com.knitml.core.model.common.StitchesOnNeedle;
import com.knitml.core.model.operations.BlockOperation;

public class ArrangeStitchesOnNeedles implements BlockOperation {
	
	protected List<StitchesOnNeedle> needles;

	public List<StitchesOnNeedle> getNeedles() {
		return needles;
	}

}
