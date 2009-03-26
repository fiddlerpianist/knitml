package com.knitml.validation.context.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.measure.Measurable;

import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.core.units.RowGauge;
import com.knitml.core.units.StitchGauge;
import com.knitml.engine.Needle;
import com.knitml.validation.context.PatternRepository;

public class DefaultPatternRepository implements PatternRepository {

	private Map<String, Instruction> globalBlockInstructions = new HashMap<String, Instruction>();
	private Map<String, Instruction> localBlockInstructions = new HashMap<String, Instruction>();
	private Map<String, InlineInstruction> inlineInstructions = new HashMap<String, InlineInstruction>();

	private Map<String, Needle> needles = new HashMap<String, Needle>();
	private Measurable<StitchGauge> stitchGauge;
	private Measurable<RowGauge> rowGauge;

	public void addNeedle(Needle needle) {
		needles.put(needle.getId(), needle);
	}

	public Needle getNeedle(String key) {
		return needles.get(key);
	}

	public Measurable<StitchGauge> getStitchGauge() {
		return stitchGauge;
	}

	public void setStitchGauge(Measurable<StitchGauge> gauge) {
		this.stitchGauge = gauge;
	}

	public Measurable<RowGauge> getRowGauge() {
		return rowGauge;
	}

	public void setRowGauge(Measurable<RowGauge> rowGauge) {
		this.rowGauge = rowGauge;
	}

	public Collection<Needle> getNeedles() {
		return needles.values();
	}

	public void addGlobalBlockInstruction(String id, Instruction instruction) {
		globalBlockInstructions.put(id, instruction);
	}

	public void addInlineInstruction(String id,
			InlineInstruction instruction) {
		inlineInstructions.put(id, instruction);
	}

	public void addLocalBlockInstruction(String id, Instruction instruction) {
		localBlockInstructions.put(id, instruction);
	}

	public void clearLocalInstructions() {
		localBlockInstructions.clear();
	}
	
	public Instruction getBlockInstruction(String key) {
		Instruction instruction = localBlockInstructions.get(key);
		if (instruction == null) {
			instruction = globalBlockInstructions.get(key);
		}
		return instruction;
	}

	public InlineInstruction getInlineInstruction(String key) {
		return inlineInstructions.get(key);
	}

	

}
