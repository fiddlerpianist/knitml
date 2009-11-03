package com.knitml.validation.context;

import java.util.Map;

import com.knitml.core.model.Identifiable;
import com.knitml.core.model.InstructionHolder;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.validation.common.InvalidStructureException;

public interface PatternState {

	boolean isReplayMode();

	void setReplayMode(boolean replay);

	boolean isWithinInstruction();

	void setWithinInstruction(boolean withinInstruction);

	void useInstruction(Instruction instruction)
			throws InvalidStructureException;

	InstructionHolder getInstructionInUse(String id);

	void removeRepeatCount(Identifiable instruction);

	void incrementRepeatCount(Identifiable instruction);

	int getHeaderRowNumber();

	void setHeaderRowNumber(int newRowNumber);

	Map<String, Integer> getInstructionRepeatCounts();

	Row getActiveRowForInstruction(String instructionId);

	void setActiveRowForInstruction(String instructionId, Row activeRow);

	/**
	 * Clears any rows which have been applied in this row. This mechanism
	 * was put in place to "fix" a particular ApplyNextRow operation to
	 * the current iteration of a row.
	 */
	void clearActiveRowsForInstructions();

}
