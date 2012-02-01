package com.knitml.core.model.directions.inline;

import java.util.Collections;
import java.util.List;

import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.directions.InlineOperation;

public class StitchGroup implements InlineOperation, CompositeOperation {

	protected List<InlineOperation> operations;
	
	public StitchGroup(List<InlineOperation> operations) {
		this.operations = operations;
	}
	
	public List<InlineOperation> getOperations() {
		return Collections.unmodifiableList(operations);
	}

}
