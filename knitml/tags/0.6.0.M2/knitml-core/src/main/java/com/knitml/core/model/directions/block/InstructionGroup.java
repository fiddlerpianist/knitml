package com.knitml.core.model.directions.block;

import java.util.List;

import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.BlockOperation;
import com.knitml.core.model.directions.CompositeOperation;


public class InstructionGroup implements BlockOperation, CompositeOperation, Identifiable {

	protected String id;
	protected String messageKey;
	protected String label;
	protected boolean resetRowCount;
	
	// must be (atomic block operation | row | instruction)* OR section*
	protected List<BlockOperation> operations;

	public InstructionGroup(String id, String label, String messageKey, boolean resetRowCount, List<BlockOperation> operations) {
		this.id = id;
		this.label = label;
		this.messageKey = messageKey;
		this.resetRowCount = resetRowCount;
		this.operations = operations;
	}
	
	
	public List<BlockOperation> getOperations() {
		return operations;
	}

	public String getId() {
		return id;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String getLabel() {
		return label;
	}

	public boolean getResetRowCount() {
		return resetRowCount;
	}
}
