package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.operations.DiscreteInlineOperation;



public class PlaceMarker implements DiscreteInlineOperation {

	public int getAdvanceCount() {
		return 0;
	}
	
	public int getIncreaseCount() {
		return 0;
	}

	public List<PlaceMarker> canonicalize() {
		List<PlaceMarker> newOps = new ArrayList<PlaceMarker>(1);
		newOps.add(new PlaceMarker());
		return newOps;
	}
	
}
