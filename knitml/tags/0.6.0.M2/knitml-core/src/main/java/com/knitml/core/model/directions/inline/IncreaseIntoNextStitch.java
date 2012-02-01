package com.knitml.core.model.directions.inline;

import java.util.List;

import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.directions.DiscreteInlineOperation;

public class IncreaseIntoNextStitch implements DiscreteInlineOperation, CompositeOperation {
	
	protected String yarnIdRef;
	protected List<DiscreteInlineOperation> operations; 
	
	public IncreaseIntoNextStitch() {
	}
	
	public IncreaseIntoNextStitch(String yarnIdRef,
			List<DiscreteInlineOperation> operations) {
		this.yarnIdRef = yarnIdRef;
		this.operations = operations;
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
	
	
}
