package com.knitml.validation.context.impl;

import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.knitml.core.model.InstructionHolder;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.InstructionGroup;
import com.knitml.core.model.directions.block.RepeatInstruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.core.model.directions.block.Section;
import com.knitml.validation.common.InvalidStructureException;
import com.knitml.validation.context.PatternState;

public class DefaultPatternState implements PatternState {

	/**
	 * The number of times an instruction has been replayed
	 */
	private int instructionReplays = 0;
	private int headerRowNumber;
	private Map<String, InstructionHolder> instructionsInUse = new LinkedHashMap<String, InstructionHolder>();
	private Map<String, Row> activeRowsForInstruction = new LinkedHashMap<String, Row>();

	/**
	 * These variables keep track of where we are in the object graph.
	 */
	private InstructionGroup currentInstructionGroup;
	private Section currentSection;
	private Instruction currentInstruction;
	private int currentInstructionRepeatCount = 0;
	private int currentInstructionRowStart = 0;
	private int currentInstructionRowOffset = 0;
	private Row currentRow;

	private int currentInstructionDepth = 0;

	public void setAsCurrent(InstructionGroup instructionGroup) {
		this.currentInstructionGroup = instructionGroup;
		this.currentSection = null;
		clearCurrentInstruction();
	}

	public void setAsCurrent(Section section) {
		this.currentSection = section;
		clearCurrentInstruction();
	}

	public void setAsCurrent(Instruction instruction) {
		this.currentInstructionDepth++;
		if (this.currentInstructionDepth == 1) {
			this.currentInstruction = instruction;
			this.currentInstructionRepeatCount = 0;
			this.currentRow = null;
			this.currentInstructionRowStart = 0;
			this.currentInstructionRowOffset = 0;
		}
	}

	public void clearCurrentInstruction() {
		if (this.currentInstructionDepth == 1) {
			this.currentInstruction = null;
			this.currentInstructionRepeatCount = 0;
			this.currentRow = null;
			this.currentInstructionRowStart = 0;
			this.currentInstructionRowOffset = 0;
		}
		this.currentInstructionDepth = max(0, currentInstructionDepth - 1);
	}

	public void setAsCurrent(Row row, Integer currentlyExecutingRowNumber) {
		if (isWithinInstruction() && currentInstruction.hasRows()) {
			if (currentRow == null) {
				// should be populated in the row by the time this method is
				// called
				currentInstructionRowStart = (row.getNumbers() != null && row
						.getNumbers().length > 0) ? row.getNumbers()[0]
						: currentlyExecutingRowNumber;
			} else {
				currentInstructionRowOffset++;
			}
			Row rowToUse = new Row();
			rowToUse.setNumber((currentInstructionRowOffset % currentInstruction
					.getRows().size()) + currentInstructionRowStart);
			this.currentRow = rowToUse;
		} else {
			this.currentRow = row;
		}
	}
	
	public boolean isAtFirstRowWithinInstruction() {
		return (isWithinInstruction() && currentInstruction.hasRows() && currentRow != null && currentInstructionRowOffset == 0);
	}

	public void clearCurrentRow() {
		if (!isWithinInstruction()) {
			this.currentRow = null;
		}
	}

	public List<Object> getLocationBreadcrumb() {
		List<Object> graph = new ArrayList<Object>();
		if (this.currentInstructionGroup != null) {
			graph.add(this.currentInstructionGroup);
		}
		if (this.currentSection != null) {
			graph.add(this.currentSection);
		}
		if (this.currentInstruction != null) {
			graph.add(this.currentInstruction);
		}
		if (this.currentInstructionRepeatCount > 0) {
			RepeatInstruction element = new RepeatInstruction();
			element.setValue(this.currentInstructionRepeatCount);
			graph.add(element);
		}
		if (this.currentRow != null) {
			graph.add(this.currentRow);
		}
		return graph;
	}

	public int nextAvailableSectionNumber() {
		return (this.currentSection != null ? this.currentSection.getNumber() + 1
				: 1);
	}

	public boolean isWithinInstruction() {
		return this.currentInstruction != null;
	}

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

	public void nextRepeatOfCurrentInstruction() {
		currentInstructionRepeatCount++;
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
