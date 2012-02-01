package com.knitml.core.model.directions.block;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.BlockOperation;
import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.directions.InlineOperation;

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
