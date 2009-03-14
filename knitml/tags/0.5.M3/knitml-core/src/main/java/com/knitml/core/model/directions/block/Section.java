package com.knitml.core.model.directions.block;

import java.util.List;

import com.knitml.core.model.directions.BlockOperation;
import com.knitml.core.model.directions.CompositeOperation;


public class Section implements BlockOperation, CompositeOperation {

	protected boolean resetRowCount;
	
	// must be (atomic block operation | row | instruction)
	protected List<BlockOperation> operations;

	public List<BlockOperation> getOperations() {
		return operations;
	}

	public boolean getResetRowCount() {
		return resetRowCount;
	}

}
