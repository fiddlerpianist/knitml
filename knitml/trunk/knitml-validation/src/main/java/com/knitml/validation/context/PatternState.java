package com.knitml.validation.context;

import java.util.List;

import com.knitml.core.model.InstructionHolder;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.InstructionGroup;
import com.knitml.core.model.directions.block.Row;
import com.knitml.core.model.directions.block.Section;
import com.knitml.validation.common.InvalidStructureException;

public interface PatternState {

	boolean isReplayMode();

	void setReplayMode(boolean replay);
	
	

	void setAsCurrent(InstructionGroup instructionGroup);
	
	void setAsCurrent(Section section);
	
	void setAsCurrent(Instruction instruction);
	
	void setAsCurrent(Row row, Integer currentlyExecutingRow);
	
	void clearCurrentRow();

	void nextRepeatOfCurrentInstruction();

	void clearCurrentInstruction();

	public int nextAvailableSectionNumber();
	
	boolean isWithinInstruction();
	
	public boolean isAtFirstRowWithinInstruction();
	
	List<Object> getLocationBreadcrumb();
	
	void useInstruction(Instruction instruction)
			throws InvalidStructureException;

	InstructionHolder getInstructionInUse(String id);

	int getHeaderRowNumber();

	void setHeaderRowNumber(int newRowNumber);

	Row getActiveRowForInstruction(String instructionId);

	void setActiveRowForInstruction(String instructionId, Row activeRow);

	/**
	 * Clears any rows which have been applied in this row. This mechanism
	 * was put in place to "fix" a particular ApplyNextRow operation to
	 * the current iteration of a row.
	 */
	void clearActiveRowsForInstructions();

}
