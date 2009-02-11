package com.knitml.core.model.directions.inline;

import java.util.List;

import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.directions.InlineOperation;

public class IncreaseIntoNextStitch implements InlineOperation, CompositeOperation {
	
	protected String yarnIdRef;
	protected List<InlineOperation> operations; 
	
	public IncreaseIntoNextStitch() {
	}
	
	public IncreaseIntoNextStitch(String yarnIdRef,
			List<InlineOperation> operations) {
		this.yarnIdRef = yarnIdRef;
		this.operations = operations;
	}

	public List<InlineOperation> getOperations() {
		return operations;
	}
	
	
}
