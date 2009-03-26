package com.knitml.core.model.directions.block;

import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.BlockOperation;


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
