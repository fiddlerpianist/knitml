package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.operations.DiscreteInlineOperation;

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

	public List<NoStitch> canonicalize() {
		int size = numberOfStitches == null ? 1 : numberOfStitches;
		List<NoStitch> newOps = new ArrayList<NoStitch>(size);
		for (int i = 0; i < size; i++) {
			newOps.add(new NoStitch());
		}
		return newOps;
	}
	
	public int getAdvanceCount() {
		return 0;
	}
	
	public int getIncreaseCount() {
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((numberOfStitches == null) ? 0 : numberOfStitches.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NoStitch other = (NoStitch) obj;
		if (numberOfStitches == null) {
			if (other.numberOfStitches != null)
				return false;
		} else if (!numberOfStitches.equals(other.numberOfStitches))
			return false;
		return true;
	}
	
}
