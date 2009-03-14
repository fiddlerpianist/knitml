package com.knitml.renderer.context;

import org.apache.commons.lang.math.Range;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.model.directions.block.Instruction;

public class InstructionInfo {

	private final Instruction instruction;
	private final String label;
	private String renderedText;
	private KnittingShape knittingShape = KnittingShape.FLAT;
	private Range rowRange;

	public InstructionInfo(Instruction instruction, String label) {
		this.instruction = instruction;
		this.label = label;
	}

	public InstructionInfo(Instruction instruction) {
		this.instruction = instruction;
		this.label = null;
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public String getLabel() {
		return label;
	}
	
	public String getId() {
		return instruction.getId();
	}

	public Range getRowRange() {
		return rowRange;
	}

	public void setRowRange(Range rowRange) {
		this.rowRange = rowRange;
	}
	public KnittingShape getKnittingShape() {
		return knittingShape;
	}

	public void setKnittingShape(KnittingShape knittingShape) {
		this.knittingShape = knittingShape;
	}

	public String getRenderedText() {
		return renderedText;
	}

	public void setRenderedText(String renderedText) {
		this.renderedText = renderedText;
	}

}
