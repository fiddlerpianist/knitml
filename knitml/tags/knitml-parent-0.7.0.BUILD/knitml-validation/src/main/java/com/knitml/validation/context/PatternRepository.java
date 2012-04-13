package com.knitml.validation.context;

import java.util.Collection;

import javax.measure.Measurable;

import com.knitml.core.model.operations.block.Instruction;
import com.knitml.core.model.operations.inline.InlineInstruction;
import com.knitml.core.units.RowGauge;
import com.knitml.core.units.StitchGauge;
import com.knitml.engine.Needle;

public interface PatternRepository {

	void addGlobalBlockInstruction(String id, Instruction instruction);
	void addInlineInstruction(String id, InlineInstruction instruction);
	void addLocalBlockInstruction(String id, Instruction instruction);
	Instruction getBlockInstruction(String id);
	InlineInstruction getInlineInstruction(String id);
	
	void clearLocalInstructions();
	
	Needle getNeedle(String name);
	Collection<Needle> getNeedles();
	
	void addNeedle(Needle needle);
	
	Measurable<StitchGauge> getStitchGauge();
	void setStitchGauge(Measurable<StitchGauge> gauge);

	Measurable<RowGauge> getRowGauge();
	void setRowGauge(Measurable<RowGauge> rowGauge);
	
}
