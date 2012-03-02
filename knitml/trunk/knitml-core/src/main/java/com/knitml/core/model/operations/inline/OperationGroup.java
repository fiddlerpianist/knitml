package com.knitml.core.model.operations.inline;

import java.util.Collections;
import java.util.List;

import com.knitml.core.model.operations.CompositeOperation;
import com.knitml.core.model.operations.InlineOperation;

public class OperationGroup implements InlineOperation, CompositeOperation {

	protected List<InlineOperation> operations;
	protected Integer size;
	
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public OperationGroup(List<InlineOperation> operations) {
		this.operations = operations;
	}
	
	public List<InlineOperation> getOperations() {
		return Collections.unmodifiableList(operations);
	}

}
