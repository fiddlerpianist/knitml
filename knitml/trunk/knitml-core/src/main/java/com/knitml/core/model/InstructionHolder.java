package com.knitml.core.model;

import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.Row;

public class InstructionHolder {
	
	private final Instruction instruction;
	private int nextRowIndex = 0;

	public Row getNextRow() {
		Row result = instruction.getRows().get(nextRowIndex);
		advanceToNextRow();
		return result;
	}

	public Row getThisRow() {
		return instruction.getRows().get(nextRowIndex);
	}
	
	protected void advanceToNextRow() {
		if (nextRowIndex == instruction.getRows().size() - 1) {
			nextRowIndex = 0;
		} else {
			nextRowIndex++;
		}
	}

	
	public InstructionHolder(Instruction instruction) throws IllegalArgumentException {
		if (instruction == null || instruction.getRows() == null || instruction.getRows().isEmpty()) {
			throw new IllegalArgumentException("An instruction stored in an InstructionHolder must have one or more rows defined in it");
		}
		this.instruction = instruction;
	}
	
	public Instruction getInstruction() {
		return instruction;
	}

}
