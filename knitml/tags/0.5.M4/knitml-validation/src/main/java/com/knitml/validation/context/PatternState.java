package com.knitml.validation.context;

import java.util.Map;

import com.knitml.core.model.Identifiable;
import com.knitml.core.model.InstructionHolder;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.validation.common.InvalidStructureException;


public interface PatternState {

	boolean isReplayMode();
	void setReplayMode(boolean replay);
	boolean isWithinInstruction();
	void setWithinInstruction(boolean withinInstruction);
	void useInstruction(Instruction instruction) throws InvalidStructureException;
	InstructionHolder getInstructionInUse(String id);
	void removeRepeatCount(Identifiable instruction);
	void incrementRepeatCount(Identifiable instruction);
	int getHeaderRowNumber();
	void setHeaderRowNumber(int newRowNumber);
	Map<String, Integer> getInstructionRepeatCounts();

}
