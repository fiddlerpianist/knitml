package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.YarnReferenceHolder;

public class WorkEven implements DiscreteInlineOperation, YarnReferenceHolder {

	protected Integer numberOfTimes;
	protected String yarnIdRef;

	public Integer getNumberOfTimes() {
		return numberOfTimes;
	}

	public String getYarnIdRef() {
		return yarnIdRef;
	}

	protected boolean hasContent() {
		return numberOfTimes != null;
	}

	public WorkEven(Integer numberOfTimes, String yarnIdRef) {
		super();
		this.numberOfTimes = numberOfTimes;
		this.yarnIdRef = yarnIdRef;
	}

	public List<WorkEven> canonicalize() {
		int size = numberOfTimes == null ? 1 : numberOfTimes;
		List<WorkEven> newOps = new ArrayList<WorkEven>(size);
		for (int i = 0; i < size; i++) {
			newOps.add(new WorkEven(1, null));
		}
		return newOps;
	}

	public int getAdvanceCount() {
		return numberOfTimes == null ? 1 : numberOfTimes;
	}

	public int getIncreaseCount() {
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((numberOfTimes == null) ? 0 : numberOfTimes.hashCode());
		result = prime * result
				+ ((yarnIdRef == null) ? 0 : yarnIdRef.hashCode());
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
		WorkEven other = (WorkEven) obj;
		if (numberOfTimes == null) {
			if (other.numberOfTimes != null)
				return false;
		} else if (!numberOfTimes.equals(other.numberOfTimes))
			return false;
		if (yarnIdRef == null) {
			if (other.yarnIdRef != null)
				return false;
		} else if (!yarnIdRef.equals(other.yarnIdRef))
			return false;
		return true;
	}

}
