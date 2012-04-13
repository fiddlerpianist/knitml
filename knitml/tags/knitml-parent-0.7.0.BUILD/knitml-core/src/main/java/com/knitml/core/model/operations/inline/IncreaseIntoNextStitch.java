package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.operations.CompositeOperation;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.YarnReferenceHolder;

public class IncreaseIntoNextStitch implements DiscreteInlineOperation,
		CompositeOperation, YarnReferenceHolder {

	protected String yarnIdRef;
	protected List<DiscreteInlineOperation> operations;

	public IncreaseIntoNextStitch() {
	}

	public IncreaseIntoNextStitch(String yarnIdRef,
			List<DiscreteInlineOperation> operations) {
		this.yarnIdRef = yarnIdRef;
		this.operations = operations;
	}

	public List<IncreaseIntoNextStitch> canonicalize() {
		List<DiscreteInlineOperation> canonicalOps = new ArrayList<DiscreteInlineOperation>();
		for (DiscreteInlineOperation op : operations) {
			List<? extends DiscreteInlineOperation> opList = op.canonicalize();
			canonicalOps.addAll(opList);
		}
		List<IncreaseIntoNextStitch> finalList = new ArrayList<IncreaseIntoNextStitch>(
				1);
		finalList.add(new IncreaseIntoNextStitch(null, canonicalOps));
		return finalList;
	}

	public List<DiscreteInlineOperation> getOperations() {
		return operations;
	}

	public int getAdvanceCount() {
		return 1;
	}

	public int getIncreaseCount() {
		int increaseSum = -1;
		for (DiscreteInlineOperation operation : operations) {
			increaseSum += operation.getAdvanceCount();
		}
		return increaseSum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((operations == null) ? 0 : operations.hashCode());
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
		IncreaseIntoNextStitch other = (IncreaseIntoNextStitch) obj;
		if (operations == null) {
			if (other.operations != null)
				return false;
		} else if (!operations.equals(other.operations))
			return false;
		if (yarnIdRef == null) {
			if (other.yarnIdRef != null)
				return false;
		} else if (!yarnIdRef.equals(other.yarnIdRef))
			return false;
		return true;
	}

	public String getYarnIdRef() {
		return yarnIdRef;
	}

}
