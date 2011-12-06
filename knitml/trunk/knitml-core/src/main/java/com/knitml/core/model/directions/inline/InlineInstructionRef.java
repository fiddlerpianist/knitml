package com.knitml.core.model.directions.inline;

import com.knitml.core.model.directions.InlineOperation;


public class InlineInstructionRef implements InlineOperation {
	
	protected InlineInstruction referencedInstruction;
	
	public InlineInstructionRef(InlineInstruction referencedInstruction) {
		this.referencedInstruction = referencedInstruction;
	}

	public InlineInstruction getReferencedInstruction() {
		return referencedInstruction;
	}

}
