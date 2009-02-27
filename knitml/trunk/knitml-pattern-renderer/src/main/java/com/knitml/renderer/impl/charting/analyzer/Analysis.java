package com.knitml.renderer.impl.charting.analyzer;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.model.directions.block.Instruction;

public class Analysis {
	private Instruction instructionToUse = null;
	private boolean chartable = false;
	private Integer maxWidth = null;
	private KnittingShape shape;
	
	public Instruction getInstructionToUse() {
		return instructionToUse;
	}
	public void setInstructionToUse(Instruction instructionToUse) {
		this.instructionToUse = instructionToUse;
	}
	public boolean isChartable() {
		return chartable;
	}
	public void setChartable(boolean chartable) {
		this.chartable = chartable;
	}
	public Integer getMaxWidth() {
		return maxWidth;
	}
	public void setMaxWidth(Integer maxWidth) {
		this.maxWidth = maxWidth;
	}
	public KnittingShape getKnittingShape() {
		return shape;
	}
	public void setKnittingShape(KnittingShape shape) {
		this.shape = shape;
	}

}
