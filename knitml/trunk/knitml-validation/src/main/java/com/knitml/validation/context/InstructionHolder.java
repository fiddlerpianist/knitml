package com.knitml.validation.context;

import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.validation.common.InvalidStructureException;

public class InstructionHolder {
	
	private final Instruction instruction;
	private int nextRowIndex = 0;

	public Row getNextRow() {
		Row result = instruction.getRows().get(nextRowIndex);
		advanceToNextRow();
		return result;
	}

	protected void advanceToNextRow() {
		if (nextRowIndex == instruction.getRows().size() - 1) {
			nextRowIndex = 0;
		} else {
			nextRowIndex++;
		}
	}

	
	public InstructionHolder(Instruction instruction) throws InvalidStructureException {
		if (instruction == null || instruction.getRows() == null || instruction.getRows().isEmpty()) {
			throw new InvalidStructureException("An instruction stored in an InstructionHolder must have one or more rows defined in it");
		}
		this.instruction = instruction;
	}
	
	public Instruction getInstruction() {
		return instruction;
	}

}
