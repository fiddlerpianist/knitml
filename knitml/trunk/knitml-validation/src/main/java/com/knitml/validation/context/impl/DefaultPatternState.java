package com.knitml.validation.context.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.knitml.core.model.Identifiable;
import com.knitml.core.model.InstructionHolder;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.validation.common.InvalidStructureException;
import com.knitml.validation.context.PatternState;

public class DefaultPatternState implements PatternState {

	/**
	 * The number of times an instruction has been replayed
	 */
	private int instructionReplays = 0;
	private int headerRowNumber;
	private boolean withinInstruction = false;
	private Map<String, InstructionHolder> instructionsInUse = new LinkedHashMap<String, InstructionHolder>();
	private Map<String, Integer> repeatCounts = new LinkedHashMap<String, Integer>();
	private Map<String, Row> activeRowsForInstruction = new LinkedHashMap<String, Row>();

	public int getHeaderRowNumber() {
		return headerRowNumber;
	}

	public void setHeaderRowNumber(int headerRowNumber) {
		this.headerRowNumber = headerRowNumber;
	}

	public void useInstruction(Instruction instruction)
			throws InvalidStructureException {
		try {
			InstructionHolder instructionHolder = new InstructionHolder(
					instruction);
			instructionsInUse.put(instruction.getId(), instructionHolder);
		} catch (IllegalArgumentException ex) {
			throw new InvalidStructureException(ex.getMessage());
		}
	}

	public InstructionHolder getInstructionInUse(String id) {
		return instructionsInUse.get(id);
	}

	public boolean isReplayMode() {
		return instructionReplays > 0;
	}

	public void setReplayMode(boolean replayMode) {
		if (replayMode) {
			instructionReplays++;
		} else if (instructionReplays > 0) {
			instructionReplays--;
		}
	}

	public boolean isWithinInstruction() {
		return withinInstruction;
	}

	public void setWithinInstruction(boolean withinInstruction) {
		this.withinInstruction = withinInstruction;
	}

	public void incrementRepeatCount(Identifiable instruction) {
		String id = instruction.getId();
		Integer count = repeatCounts.get(instruction.getId());
		if (count == null) {
			count = 0;
		}
		count++;
		repeatCounts.put(id, count);
	}

	public void removeRepeatCount(Identifiable instruction) {
		repeatCounts.remove(instruction.getId());
	}

	public Map<String, Integer> getInstructionRepeatCounts() {
		return Collections.unmodifiableMap(repeatCounts);
	}

	public void clearActiveRowsForInstructions() {
		this.activeRowsForInstruction.clear();
	}

	public Row getActiveRowForInstruction(String instructionId) {
		return this.activeRowsForInstruction.get(instructionId);
	}

	public void setActiveRowForInstruction(String instructionId, Row activeRow) {
		this.activeRowsForInstruction.put(instructionId, activeRow);
	}

}
