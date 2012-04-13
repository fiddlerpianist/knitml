package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.operations.DiscreteInlineOperation;

public class PassPreviousStitchOver implements DiscreteInlineOperation {

	protected Integer numberOfTimes;

	public PassPreviousStitchOver() {
	}

	public PassPreviousStitchOver(int numberOfStitches) {
		this.numberOfTimes = numberOfStitches;
	}

	public List<PassPreviousStitchOver> canonicalize() {
		int size = numberOfTimes == null ? 1 : numberOfTimes;
		List<PassPreviousStitchOver> newOps = new ArrayList<PassPreviousStitchOver>(size);
		for (int i = 0; i < size; i++) {
			newOps.add(new PassPreviousStitchOver());
		}
		return newOps;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((numberOfTimes == null) ? 0 : numberOfTimes.hashCode());
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
		PassPreviousStitchOver other = (PassPreviousStitchOver) obj;
		if (numberOfTimes == null) {
			if (other.numberOfTimes != null)
				return false;
		} else if (!numberOfTimes.equals(other.numberOfTimes))
			return false;
		return true;
	}

}
