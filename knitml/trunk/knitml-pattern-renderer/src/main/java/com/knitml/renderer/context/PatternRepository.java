package com.knitml.renderer.context;

import java.util.List;

import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.core.model.header.Needle;
import com.knitml.core.model.header.Yarn;

public interface PatternRepository {

	Yarn getYarn(String id);
	void addYarn(Yarn yarn);
	Needle getNeedle(String id);
	void addNeedle(Needle needle);
	
	void addLocalInstruction(Instruction instruction);
	void addGlobalInstruction(Instruction instruction, String label);
	void clearLocalInstructions();
	InstructionInfo getInstruction(String instructionId);
	
	void addInlineInstruction(InlineInstruction instruction);
	InlineInstruction getInlineInstruction(String instructionId);
	
	void setPatternMessageSources(List<String> messageSourceLocations);
	String getPatternMessage(String key);
	String getPatternMessage(String key, String defaultValue);
	
}
