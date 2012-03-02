package com.knitml.core.model.operations.inline;

import com.knitml.core.model.common.Identifiable;
import com.knitml.core.model.operations.InlineOperation;


public class ApplyNextRow implements InlineOperation {
	
	protected Identifiable instructionRef;

	public ApplyNextRow(Identifiable instructionRef) {
		this.instructionRef = instructionRef;
	}

	public Identifiable getInstructionRef() {
		return instructionRef;
	}

}
