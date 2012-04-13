package com.knitml.core.model.operations.block;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.ValidationException;
import com.knitml.core.model.common.Identifiable;
import com.knitml.core.model.operations.BlockOperation;
import com.knitml.core.model.operations.CompositeOperation;
import com.knitml.core.model.operations.Operation;
import com.knitml.core.model.operations.chart.ChartInfo;

public class Instruction implements BlockOperation, Identifiable,
		CompositeOperation {

	// an instruction may have either an eachRowOf or a list of rows, not both
	protected List<Row> rows = new ArrayList<Row>();
	protected ForEachRowInInstruction forEachRowInInstruction;

	public ChartInfo getChartInfo() {
		return chartInfo;
	}

	protected String id;
	protected String label;
	protected String messageKey;
	protected KnittingShape knittingShape;
	protected Integer rowCount;
	protected Integer startingStitchCount;
	protected ChartInfo chartInfo;

	public void validate() {
		if (forEachRowInInstruction != null && hasRows()) {
			throw new ValidationException(
					"Only 'forEachRowInInstruction' or a series of 'row' elements may be specified; not both");
		}
	}
	
	public boolean hasRows() {
		return rows != null && !rows.isEmpty();
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
		this.knittingShape = instruction.getKnittingShape();
		this.rowCount = instruction.getRowCount();
		this.startingStitchCount = instruction.getStartingStitchCount();
		this.chartInfo = instruction.getChartInfo();
		this.rows = rows;
	}
	
	public Instruction(String id, String label, String messageKey, ForEachRowInInstruction forEachRow) {
		this.id = id;
		this.label = label;
		this.messageKey = messageKey;
		this.forEachRowInInstruction = forEachRow;
	}
	
	public Instruction(String id, String label, String messageKey, List<Row> rows) {
		this.id = id;
		this.label = label;
		this.messageKey = messageKey;
		this.rows = rows;
	}

	public Instruction(String id, String label, String messageKey, KnittingShape knittingShape, List<Row> rows) {
		this.id = id;
		this.label = label;
		this.messageKey = messageKey;
		this.knittingShape = knittingShape;
		this.rows = rows;
	}
	
	public Instruction(String id, String label, String messageKey, KnittingShape knittingShape, List<Row> rows, Integer rowCount, Integer startingStitchCount, ChartInfo chartInfo) {
		this.id = id;
		this.label = label;
		this.messageKey = messageKey;
		this.knittingShape = knittingShape;
		this.rows = rows;
		this.rowCount = rowCount;
		this.startingStitchCount = startingStitchCount;
		this.chartInfo = chartInfo;
	}
	
	public KnittingShape getKnittingShape() {
		return knittingShape;
	}
	
	public Integer getRowCount() {
		return rowCount;
	}

	public Integer getStartingStitchCount() {
		return startingStitchCount;
	}

}
