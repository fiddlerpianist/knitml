package com.knitml.renderer.context;

import java.io.Writer;
import java.util.List;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.StitchesOnNeedle;
import com.knitml.core.model.directions.block.CastOn;
import com.knitml.core.model.directions.block.DeclareFlatKnitting;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.RepeatInstruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.core.model.directions.block.Section;
import com.knitml.core.model.directions.information.NumberOfStitches;
import com.knitml.core.model.directions.inline.ApplyNextRow;
import com.knitml.core.model.directions.inline.BindOff;
import com.knitml.core.model.directions.inline.BindOffAll;
import com.knitml.core.model.directions.inline.CrossStitches;
import com.knitml.core.model.directions.inline.Decrease;
import com.knitml.core.model.directions.inline.Increase;
import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.core.model.directions.inline.InlineInstructionRef;
import com.knitml.core.model.directions.inline.Knit;
import com.knitml.core.model.directions.inline.InlinePickUpStitches;
import com.knitml.core.model.directions.inline.Purl;
import com.knitml.core.model.directions.inline.Repeat;
import com.knitml.core.model.directions.inline.Slip;
import com.knitml.core.model.header.GeneralInformation;
import com.knitml.core.model.header.Needle;
import com.knitml.core.model.header.Supplies;
import com.knitml.core.model.header.Yarn;

public interface Renderer {
	
	// header events
	void addYarn(Yarn yarn);
	void renderGeneralInformation(GeneralInformation generalInformation);
	void renderSupplies(Supplies supplies);

	void beginInstructionDefinitions();
	void endInstructionDefinitions();

	void beginInstructionDefinition(Instruction instruction, String label);
	void endInstructionDefinition();
	void beginInlineInstructionDefinition(InlineInstruction instruction, String label);
	void endInlineInstructionDefinition(InlineInstruction instruction);
	
	// body events
	void beginInlineInstruction(InlineInstruction instruction);
	void endInlineInstruction(InlineInstruction instruction);
	
	// block events
	void beginDirections();
	void endDirections();

	void beginSection(Section section);
	void endSection();
	
	void beginInstructionGroup(String label);
	void beginInstructionGroup();
	void endInstructionGroup();

	Instruction evaluateInstruction(Instruction instruction);
	void beginInstruction(Instruction instruction, String label);
	void endInstruction();
	
	boolean renderInlineInstructionRef(InlineInstructionRef instructionRef, String label);

	void renderRepeatInstruction(RepeatInstruction repeatInstruction, InstructionInfo instructionInfo);
	void renderUseNeedles(List<Needle> needles);
	void renderCastOn(CastOn castOn);
	void renderUsingNeedlesCastOn(List<Needle> needles,
			CastOn castOn);
	void renderJoinInRound();
	void renderDeclareFlatKnitting(DeclareFlatKnitting spec);
	void renderDeclareRoundKnitting();
	void renderArrangeStitchesOnNeedles(List<StitchesOnNeedle> needles);
	void renderUnworkedStitches(int number);

	void beginRow();
	void endRow(Row row, KnittingShape shape);

	// inline events
	void renderKnit(Knit knit);
	void renderPurl(Purl purl);
	void renderDecrease(Decrease decrease);
	void renderIncrease(Increase increase);
	void renderSlip(Slip slip);
	void renderCrossStitches(CrossStitches element);
	void renderDesignateEndOfRow(KnittingShape currentKnittingShape);
	void renderTurn();
	void renderPickUpStitches(InlinePickUpStitches pickUpStitches);
	void renderPlaceMarker();
	void renderRemoveMarker();
	void renderGraftStitchesTogether(List<Needle> needles);
	void renderBindOff(BindOff bindOff);
	void renderBindOffAll(BindOffAll bindOff);
	void renderApplyNextRow(ApplyNextRow applyNextRow, String label);

	void beginRepeat(Repeat repeat);
	void endRepeat(Repeat.Until until, Integer value);

	void beginUsingNeedle(Needle needle);
	void endUsingNeedle();

	// hybrid events
	void beginInformation();
	void endInformation();

	void renderMessage(String messageToRender);

	// administrative setters
	void setWriter(Writer writer);
	void setRenderingContext(RenderingContext renderingContext);
	void renderNumberOfStitchesInRow(NumberOfStitches numberOfStitches);
}
