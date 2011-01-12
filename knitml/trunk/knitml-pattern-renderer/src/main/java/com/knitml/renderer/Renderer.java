package com.knitml.renderer;

import java.util.List;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.StitchesOnNeedle;
import com.knitml.core.model.Pattern;
import com.knitml.core.model.directions.block.CastOn;
import com.knitml.core.model.directions.block.DeclareFlatKnitting;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.PickUpStitches;
import com.knitml.core.model.directions.block.RepeatInstruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.core.model.directions.block.Section;
import com.knitml.core.model.directions.information.NumberOfStitches;
import com.knitml.core.model.directions.inline.ApplyNextRow;
import com.knitml.core.model.directions.inline.BindOff;
import com.knitml.core.model.directions.inline.BindOffAll;
import com.knitml.core.model.directions.inline.CrossStitches;
import com.knitml.core.model.directions.inline.Decrease;
import com.knitml.core.model.directions.inline.FromStitchHolder;
import com.knitml.core.model.directions.inline.Increase;
import com.knitml.core.model.directions.inline.IncreaseIntoNextStitch;
import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.core.model.directions.inline.InlineInstructionRef;
import com.knitml.core.model.directions.inline.InlinePickUpStitches;
import com.knitml.core.model.directions.inline.Knit;
import com.knitml.core.model.directions.inline.NoStitch;
import com.knitml.core.model.directions.inline.PassPreviousStitchOver;
import com.knitml.core.model.directions.inline.Purl;
import com.knitml.core.model.directions.inline.Repeat;
import com.knitml.core.model.directions.inline.Slip;
import com.knitml.core.model.directions.inline.SlipToStitchHolder;
import com.knitml.core.model.directions.inline.WorkEven;
import com.knitml.core.model.header.GeneralInformation;
import com.knitml.core.model.header.Needle;
import com.knitml.core.model.header.Supplies;
import com.knitml.core.model.header.Yarn;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.RenderingContext;

public interface Renderer {
	
	// header events
	
	void beginPattern(Pattern pattern);
	void endPattern();
	void addYarn(Yarn yarn);
	void renderGeneralInformation(GeneralInformation generalInformation);
	void renderSupplies(Supplies supplies);

	void beginInstructionDefinitions();
	void endInstructionDefinitions();

	void beginInstructionDefinition(InstructionInfo instructionInfo);
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

	Instruction evaluateInstruction(Instruction instruction, RepeatInstruction associatedRepeatInstruction);
	Instruction evaluateInstructionDefinition(Instruction instruction);
	
	void beginInstruction(InstructionInfo instructionInfo);
	void endInstruction();
	
	void renderInlineInstructionRef(InlineInstructionRef instructionRef, String label);
	void renderRepeatInstruction(RepeatInstruction repeatInstruction, InstructionInfo instructionInfo);
	void renderUseNeedles(List<Needle> needles);
	void renderCastOn(CastOn castOn);
	void renderPickUpStitches(PickUpStitches pickUpStitches);
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
	void renderWorkEven(WorkEven workEven);
	void renderDecrease(Decrease decrease);
	void renderPassPreviousStitchOver(PassPreviousStitchOver ppso);
	void renderIncrease(Increase increase);
	void renderSlip(Slip slip);
	void renderNoStitch(NoStitch noStitch);
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
	void renderSlipToStitchHolder(SlipToStitchHolder operation);

	// composite inline events
	void beginRepeat(Repeat repeat);
	void endRepeat(Repeat.Until until, Integer value);

	void beginFromStitchHolder(FromStitchHolder fromStitchHolder);
	void endFromStitchHolder(FromStitchHolder fromStitchHolder);

	void beginIncreaseIntoNextStitch(IncreaseIntoNextStitch increaseIntoNextStitch);
	void endIncreaseIntoNextStitch(IncreaseIntoNextStitch increaseIntoNextStitch);

	void beginUsingNeedle(Needle needle);
	void endUsingNeedle();

	// hybrid events
	void beginInformation();
	void endInformation();

	void renderMessage(String messageToRender);
	void renderNumberOfStitchesInRow(NumberOfStitches numberOfStitches);

	RenderingContext getRenderingContext();
}
