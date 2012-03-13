package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.DecreaseType;

public class DoubleDecrease extends Decrease {

	@Override
	public int getAdvanceCount() {
		int numberOfDecreases = numberOfTimes == null ? 1 : numberOfTimes;
		return 3 * numberOfDecreases;
	}

	@Override
	public int getIncreaseCount() {
		int numberOfDecreases = numberOfTimes == null ? 1 : numberOfTimes;
		return -2 * numberOfDecreases;
	}

	public DoubleDecrease(Integer numberOfTimes, DecreaseType type, String yarnIdRef) {
		this.numberOfTimes = numberOfTimes;
		this.type = type;
		this.yarnIdRef = yarnIdRef;
	}
	
	public List<DoubleDecrease> canonicalize() {
		int size = numberOfTimes == null ? 1 : numberOfTimes;
		List<DoubleDecrease> newOps = new ArrayList<DoubleDecrease>(size);
		for (int i = 0; i < size; i++) {
			newOps.add(new DoubleDecrease(1, this.type, null));
		}
		return newOps;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((numberOfTimes == null) ? 0 : numberOfTimes.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result
					+ ((yarnIdRef == null) ? 0 : yarnIdRef.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DoubleDecrease other = (DoubleDecrease) obj;
		if (numberOfTimes == null) {
			if (other.numberOfTimes != null)
				return false;
		} else if (!numberOfTimes.equals(other.numberOfTimes))
			return false;
		if (type != other.type)
			return false;
		if (yarnIdRef == null) {
			if (other.yarnIdRef != null)
				return false;
		} else if (!yarnIdRef.equals(other.yarnIdRef))
			return false;
		return true;
	}

}
