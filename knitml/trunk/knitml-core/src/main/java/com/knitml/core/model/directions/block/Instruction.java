package com.knitml.core.model.directions.block;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.ValidationException;
import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.BlockOperation;
import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.directions.Operation;

public class Instruction implements BlockOperation, Identifiable,
		CompositeOperation {

	// an instruction may have either an eachRowOf or a list of rows, not both
	protected List<Row> rows = new ArrayList<Row>();
	protected ForEachRowInInstruction forEachRowInInstruction;

	protected String id;
	protected String label;
	protected String messageKey;

	public void validate() {
		if (forEachRowInInstruction != null && rows != null && !rows.isEmpty()) {
			throw new ValidationException(
					"Only 'forEachRowInInstruction' or a series of 'row' elements may be specified; not both");
		}
	}
	
	public boolean hasRows() {
		return forEachRowInInstruction == null;
	}
	
	public boolean hasLabelOrMessageKey() {
		return (label != null || messageKey != null);
	}

	public ForEachRowInInstruction getForEachRowInInstruction() {
		return forEachRowInInstruction;
	}

	public String getLabel() {
		return label;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public List<Row> getRows() {
		return rows;
	}

	public String getId() {
		return id;
	}

	public List<? extends Operation> getOperations() {
		return getRows();
	}

	public Instruction(Instruction instruction, List<Row> rows) {
		this.id = instruction.getId();
		this.label = instruction.getLabel();
		this.messageKey = instruction.getMessageKey();
		this.rows = rows;
	}
	
	public Instruction(String id, String label, String messageKey, List<Row> rows) {
		this.id = id;
		this.label = label;
		this.messageKey = messageKey;
		this.rows = rows;
	}
	
}
