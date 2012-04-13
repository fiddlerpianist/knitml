package com.knitml.core.model.operations.block;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.common.Identifiable;
import com.knitml.core.model.operations.BlockOperation;
import com.knitml.core.model.operations.CompositeOperation;
import com.knitml.core.model.operations.InlineOperation;

public class ForEachRowInInstruction implements BlockOperation, CompositeOperation {
	
	protected Identifiable ref;
	protected List<InlineOperation> operations = new ArrayList<InlineOperation>();
	
	public ForEachRowInInstruction() {
	}
	
	public ForEachRowInInstruction(Identifiable instructionRef, List<InlineOperation> operations) {
		this.ref = instructionRef;
		this.operations = operations;
	}

	public Identifiable getRef() {
		return ref;
	}

	public List<InlineOperation> getOperations() {
		return operations;
	}
}
