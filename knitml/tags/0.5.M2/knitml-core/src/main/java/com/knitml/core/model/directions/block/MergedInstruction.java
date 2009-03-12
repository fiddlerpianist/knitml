package com.knitml.core.model.directions.block;

import java.util.List;

import com.knitml.core.common.MergePoint;
import com.knitml.core.common.MergeType;
import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.BlockOperation;


public class MergedInstruction implements BlockOperation, Identifiable {
	
	protected String id;
	protected String label;
	protected String messageKey;
	protected List<InstructionRef> instructions;
	protected MergePoint mergePoint;
	protected MergeType type;
	
	public String getLabel() {
		return label;
	}
	public String getMessageKey() {
		return messageKey;
	}
	public String getId() {
		return id;
	}
	public List<InstructionRef> getInstructions() {
		return instructions;
	}
	public MergePoint getMergePoint() {
		return mergePoint;
	}
	public MergeType getType() {
		return type;
	}
	
}
