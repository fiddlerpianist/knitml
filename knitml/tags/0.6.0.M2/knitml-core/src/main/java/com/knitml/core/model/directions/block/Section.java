package com.knitml.core.model.directions.block;

import java.util.List;

import com.knitml.core.model.directions.BlockOperation;
import com.knitml.core.model.directions.CompositeOperation;

public class Section implements BlockOperation, CompositeOperation {

	protected boolean resetRowCount;
	// number is an internal count keeping track of the section number
	// relative to its context. Examples: Section 1 of Instruction Group A,
	// Section 3 of Instruction Group B, Section 3 of Instruction Group C.
	// Not serialized to/from XML.
	private Integer number;

	// must be (atomic block operation | row | instruction)
	protected List<BlockOperation> operations;

	public void setNumber(int number) {
		this.number = number;
	}

	public Integer getNumber() {
		return this.number;
	}

	public List<BlockOperation> getOperations() {
		return operations;
	}

	public boolean getResetRowCount() {
		return resetRowCount;
	}

}
