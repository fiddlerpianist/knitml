package com.knitml.core.model.directions.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.directions.InlineOperation;

public class InlineInstruction implements InlineOperation, CompositeOperation, Identifiable {
	
	protected List<InlineOperation> operations = new ArrayList<InlineOperation>();
	protected String id;
	protected String label;
	protected String messageKey;
	
	public String getLabel() {
		return label;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String getId() {
		return id;
	}

	public List<InlineOperation> getOperations() {
		return operations;
	}
	
	public InlineInstruction() {
	}
	
	public InlineInstruction(InlineInstruction inlineInstructionToCopy, List<InlineOperation> operations) {
		this.id = inlineInstructionToCopy.getId();
		this.label = inlineInstructionToCopy.getLabel();
		this.messageKey = inlineInstructionToCopy.getMessageKey();
		this.operations = operations;
	}

}
