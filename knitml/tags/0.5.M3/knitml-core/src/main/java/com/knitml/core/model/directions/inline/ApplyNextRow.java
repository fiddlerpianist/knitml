package com.knitml.core.model.directions.inline;

import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.InlineOperation;


public class ApplyNextRow implements InlineOperation {
	
	protected Identifiable instructionRef;

	public ApplyNextRow(Identifiable instructionRef) {
		this.instructionRef = instructionRef;
	}

	public Identifiable getInstructionRef() {
		return instructionRef;
	}

}
