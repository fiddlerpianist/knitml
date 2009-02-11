package com.knitml.renderer.context;

import org.apache.commons.lang.math.Range;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.block.Instruction;

public class InstructionInfo {

	private final Instruction instruction;
	private final String label;
	private KnittingShape knittingShape = KnittingShape.FLAT;
	// private SortedSet<Integer> rows = new TreeSet<Integer>();
	private Range rowRange;

	public InstructionInfo(Instruction instruction, String label) {
		this.instruction = instruction;
		this.label = label;
	}

	public InstructionInfo(Instruction instruction) {
		this.instruction = instruction;
		this.label = null;
	}

	public Identifiable getInstruction() {
		return instruction;
	}

	public String getLabel() {
		return label;
	}

	public Range getRowRange() {
		return rowRange;
	}

	public void setRowRange(Range rowRange) {
		this.rowRange = rowRange;
	}
	//
	// public void addRows(Collection<Integer> rowsToAdd) {
	// if (rowsToAdd != null) {
	// this.rows.addAll(rowsToAdd);
	// }
	// }
	//	
	// public void addRowArray(int[] rowsToAdd) {
	// if (rowsToAdd != null) {
	// for (Integer rowToAdd : rowsToAdd) {
	// this.rows.add(rowToAdd);
	// }
	// }
	// }

	// public Range getRowRange() {
	// if (!areRowsConsecutive()) {
	// throw new IllegalStateException(
	// "Cannot return a row range of non-consecutive rows");
	// }
	// return new IntRange(rows.first(), rows.last());
	// }
	//
	// public boolean areRowsConsecutive() {
	// if (rows.size() == 0) {
	// return true;
	// }
	// int previous = rows.first();
	// for (int i : rows) {
	// if (i == previous) {
	// continue;
	// }
	// if (i != (previous + 1)) {
	// return false;
	// }
	// previous = i;
	// }
	// return true;
	// }

	public KnittingShape getKnittingShape() {
		return knittingShape;
	}

	public void setKnittingShape(KnittingShape knittingShape) {
		this.knittingShape = knittingShape;
	}

	// public SortedSet<Integer> getRows() {
	// return rows;
	// }

}
