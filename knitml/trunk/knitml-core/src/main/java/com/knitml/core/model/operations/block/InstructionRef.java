package com.knitml.core.model.operations.block;

import com.knitml.core.model.common.Identifiable;
import com.knitml.core.model.operations.BlockOperation;


public class InstructionRef implements BlockOperation {
	
	protected Identifiable ref;

	public Identifiable getRef() {
		return ref;
	}
	
	public InstructionRef() {
	}
	
	public InstructionRef(Identifiable ref) {
		this.ref = ref;
	}

}
