package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.operations.DiscreteInlineOperation;

public class RemoveMarker implements DiscreteInlineOperation {

	public int getAdvanceCount() {
		return 0;
	}
	
	public int getIncreaseCount() {
		return 0;
	}

	public List<RemoveMarker> canonicalize() {
		List<RemoveMarker> newOps = new ArrayList<RemoveMarker>(1);
		newOps.add(new RemoveMarker());
		return newOps;
	}

}
