package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.knitml.core.model.operations.CompositeOperation;
import com.knitml.core.model.operations.DiscreteInlineOperation;

public class OperationGroup implements DiscreteInlineOperation, CompositeOperation {

	protected List<DiscreteInlineOperation> operations;
	protected Integer size;
	
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public OperationGroup(Integer size, List<DiscreteInlineOperation> operations) {
		this(operations);
		this.size = size;
	}
	public OperationGroup(List<DiscreteInlineOperation> operations) {
		this.operations = operations;
	}
	
	public OperationGroup canonicalizeGroup() {
		return canonicalize().get(0);
	}
	
	public List<OperationGroup> canonicalize() {
		List<DiscreteInlineOperation> canonicalOps = new ArrayList<DiscreteInlineOperation>();
		for (DiscreteInlineOperation op : operations) {
			List<? extends DiscreteInlineOperation> opList = op.canonicalize();
			canonicalOps.addAll(opList);
		}
		List<OperationGroup> operationGroupList = new ArrayList<OperationGroup>(1);
		OperationGroup newGroup = new OperationGroup(this.size, canonicalOps); 
		operationGroupList.add(newGroup);
		return operationGroupList;
	}

	public List<DiscreteInlineOperation> getOperations() {
		return Collections.unmodifiableList(operations);
	}
	
	public boolean isCableInstruction() {
		return operations.size() > 1 && operations.get(0) instanceof CrossStitches;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((operations == null) ? 0 : operations.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
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
		OperationGroup other = (OperationGroup) obj;
		if (operations == null) {
			if (other.operations != null)
				return false;
		} else if (!operations.equals(other.operations))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		return true;
	}

	public int getAdvanceCount() {
		int i = 0;
		for (DiscreteInlineOperation operation : operations) {
			i += operation.getAdvanceCount();
		}
		return i;
	}

	public int getIncreaseCount() {
		int i = 0;
		for (DiscreteInlineOperation operation : operations) {
			i += operation.getIncreaseCount();
		}
		return i;
	}

}
