package test.support;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.knitml.core.common.KnittingShape;
import com.knitml.core.model.common.Needle;
import com.knitml.core.model.common.StitchesOnNeedle;
import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.operations.block.CastOn;
import com.knitml.core.model.operations.block.DeclareFlatKnitting;
import com.knitml.core.model.operations.block.Instruction;
import com.knitml.core.model.operations.block.PickUpStitches;
import com.knitml.core.model.operations.block.RepeatInstruction;
import com.knitml.core.model.operations.block.Row;
import com.knitml.core.model.operations.information.NumberOfStitches;
import com.knitml.core.model.operations.inline.ApplyNextRow;
import com.knitml.core.model.operations.inline.BindOff;
import com.knitml.core.model.operations.inline.BindOffAll;
import com.knitml.core.model.operations.inline.CrossStitches;
import com.knitml.core.model.operations.inline.Decrease;
import com.knitml.core.model.operations.inline.FromStitchHolder;
import com.knitml.core.model.operations.inline.Increase;
import com.knitml.core.model.operations.inline.IncreaseIntoNextStitch;
import com.knitml.core.model.operations.inline.InlineCastOn;
import com.knitml.core.model.operations.inline.InlineInstruction;
import com.knitml.core.model.operations.inline.InlineInstructionRef;
import com.knitml.core.model.operations.inline.InlinePickUpStitches;
import com.knitml.core.model.operations.inline.Knit;
import com.knitml.core.model.operations.inline.MultipleDecrease;
import com.knitml.core.model.operations.inline.NoStitch;
import com.knitml.core.model.operations.inline.OperationGroup;
import com.knitml.core.model.operations.inline.PassPreviousStitchOver;
import com.knitml.core.model.operations.inline.Purl;
import com.knitml.core.model.operations.inline.Repeat;
import com.knitml.core.model.operations.inline.Repeat.Until;
import com.knitml.core.model.operations.inline.Slip;
import com.knitml.core.model.operations.inline.SlipToStitchHolder;
import com.knitml.core.model.operations.inline.WorkEven;
import com.knitml.core.model.pattern.GeneralInformation;
import com.knitml.core.model.pattern.Pattern;
import com.knitml.core.model.pattern.Section;
import com.knitml.core.model.pattern.Supplies;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.RenderingContext;

public class NoOpWrapperRenderer implements Renderer {
	
	@Inject
	public NoOpWrapperRenderer(@Assisted Renderer renderer) {
	}

	public void beginPattern(Pattern pattern) {
		// TODO Auto-generated method stub
		
	}

	public void endPattern() {
		// TODO Auto-generated method stub
		
	}

	public void addYarn(Yarn yarn) {
		// TODO Auto-generated method stub
		
	}

	public void renderGeneralInformation(GeneralInformation generalInformation) {
		// TODO Auto-generated method stub
		
	}

	public void renderSupplies(Supplies supplies) {
		// TODO Auto-generated method stub
		
	}

	public void beginInstructionDefinitions() {
		// TODO Auto-generated method stub
		
	}

	public void endInstructionDefinitions() {
		// TODO Auto-generated method stub
		
	}

	public void beginInstructionDefinition(InstructionInfo instructionInfo) {
		// TODO Auto-generated method stub
		
	}

	public void endInstructionDefinition() {
		// TODO Auto-generated method stub
		
	}

	public void beginInlineInstructionDefinition(InlineInstruction instruction,
			String label) {
		// TODO Auto-generated method stub
		
	}

	public void endInlineInstructionDefinition(InlineInstruction instruction) {
		// TODO Auto-generated method stub
		
	}

	public void beginInlineInstruction(InlineInstruction instruction) {
		// TODO Auto-generated method stub
		
	}

	public void endInlineInstruction(InlineInstruction instruction) {
		// TODO Auto-generated method stub
		
	}

	public void beginDirections() {
		// TODO Auto-generated method stub
		
	}

	public void endDirections() {
		// TODO Auto-generated method stub
		
	}

	public void beginSection(Section section) {
		// TODO Auto-generated method stub
		
	}

	public void endSection() {
		// TODO Auto-generated method stub
		
	}

	public void beginInstructionGroup(String label) {
		// TODO Auto-generated method stub
		
	}

	public void beginInstructionGroup() {
		// TODO Auto-generated method stub
		
	}

	public void endInstructionGroup() {
		// TODO Auto-generated method stub
		
	}

	public Instruction evaluateInstruction(Instruction instruction,
			RepeatInstruction associatedRepeatInstruction) {
		// TODO Auto-generated method stub
		return null;
	}

	public Instruction evaluateInstructionDefinition(Instruction instruction) {
		// TODO Auto-generated method stub
		return null;
	}

	public void beginInstruction(InstructionInfo instructionInfo) {
		// TODO Auto-generated method stub
		
	}

	public void endInstruction() {
		// TODO Auto-generated method stub
		
	}

	public void renderInlineInstructionRef(InlineInstructionRef instructionRef,
			String label) {
		// TODO Auto-generated method stub
		
	}

	public void renderRepeatInstruction(RepeatInstruction repeatInstruction,
			InstructionInfo instructionInfo) {
		// TODO Auto-generated method stub
		
	}

	public void renderUseNeedles(List<Needle> needles) {
		// TODO Auto-generated method stub
		
	}

	public void renderCastOn(CastOn castOn) {
		// TODO Auto-generated method stub
		
	}

	public void renderPickUpStitches(PickUpStitches pickUpStitches) {
		// TODO Auto-generated method stub
		
	}

	public void renderUsingNeedlesCastOn(List<Needle> needles, CastOn castOn) {
		// TODO Auto-generated method stub
		
	}

	public void renderJoinInRound() {
		// TODO Auto-generated method stub
		
	}

	public void renderDeclareFlatKnitting(DeclareFlatKnitting spec) {
		// TODO Auto-generated method stub
		
	}

	public void renderDeclareRoundKnitting() {
		// TODO Auto-generated method stub
		
	}

	public void renderArrangeStitchesOnNeedles(List<StitchesOnNeedle> needles) {
		// TODO Auto-generated method stub
		
	}

	public void renderUnworkedStitches(int number) {
		// TODO Auto-generated method stub
		
	}

	public void beginRow() {
		// TODO Auto-generated method stub
		
	}

	public void endRow(Row row, KnittingShape shape) {
		// TODO Auto-generated method stub
		
	}

	public void renderKnit(Knit knit) {
		// TODO Auto-generated method stub
		
	}

	public void renderPurl(Purl purl) {
		// TODO Auto-generated method stub
		
	}

	public void renderWorkEven(WorkEven workEven) {
		// TODO Auto-generated method stub
		
	}

	public void renderDecrease(Decrease decrease) {
		// TODO Auto-generated method stub
		
	}

	public void renderPassPreviousStitchOver(PassPreviousStitchOver ppso) {
		// TODO Auto-generated method stub
		
	}

	public void renderIncrease(Increase increase) {
		// TODO Auto-generated method stub
		
	}

	public void renderSlip(Slip slip) {
		// TODO Auto-generated method stub
		
	}

	public void renderNoStitch(NoStitch noStitch) {
		// TODO Auto-generated method stub
		
	}

	public void renderCrossStitches(CrossStitches element) {
		// TODO Auto-generated method stub
		
	}

	public void renderDesignateEndOfRow(KnittingShape currentKnittingShape) {
		// TODO Auto-generated method stub
		
	}

	public void renderTurn() {
		// TODO Auto-generated method stub
		
	}

	public void renderCastOn(InlineCastOn castOn) {
		// TODO Auto-generated method stub
		
	}

	public void renderPickUpStitches(InlinePickUpStitches pickUpStitches) {
		// TODO Auto-generated method stub
		
	}

	public void renderPlaceMarker() {
		// TODO Auto-generated method stub
		
	}

	public void renderRemoveMarker() {
		// TODO Auto-generated method stub
		
	}

	public void renderGraftStitchesTogether(List<Needle> needles) {
		// TODO Auto-generated method stub
		
	}

	public void renderBindOff(BindOff bindOff) {
		// TODO Auto-generated method stub
		
	}

	public void renderBindOffAll(BindOffAll bindOff) {
		// TODO Auto-generated method stub
		
	}

	public void renderApplyNextRow(ApplyNextRow applyNextRow, String label) {
		// TODO Auto-generated method stub
		
	}

	public void renderSlipToStitchHolder(SlipToStitchHolder operation) {
		// TODO Auto-generated method stub
		
	}

	public void beginRepeat(Repeat repeat) {
		// TODO Auto-generated method stub
		
	}

	public void endRepeat(Until until, Integer value) {
		// TODO Auto-generated method stub
		
	}

	public void beginFromStitchHolder(FromStitchHolder fromStitchHolder) {
		// TODO Auto-generated method stub
		
	}

	public void endFromStitchHolder(FromStitchHolder fromStitchHolder) {
		// TODO Auto-generated method stub
		
	}

	public boolean beginIncreaseIntoNextStitch(
			IncreaseIntoNextStitch increaseIntoNextStitch) {
		// TODO Auto-generated method stub
		return false;
	}

	public void endIncreaseIntoNextStitch(
			IncreaseIntoNextStitch increaseIntoNextStitch) {
		// TODO Auto-generated method stub
		
	}

	public void beginUsingNeedle(Needle needle) {
		// TODO Auto-generated method stub
		
	}

	public void endUsingNeedle() {
		// TODO Auto-generated method stub
		
	}

	public boolean beginOperationGroup(OperationGroup group) {
		// TODO Auto-generated method stub
		return false;
	}

	public void endOperationGroup(OperationGroup group) {
		// TODO Auto-generated method stub
		
	}

	public void beginInformation() {
		// TODO Auto-generated method stub
		
	}

	public void endInformation() {
		// TODO Auto-generated method stub
		
	}

	public void renderMessage(String messageToRender) {
		// TODO Auto-generated method stub
		
	}

	public void renderNumberOfStitchesInRow(NumberOfStitches numberOfStitches) {
		// TODO Auto-generated method stub
		
	}

	public RenderingContext getRenderingContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public void renderMultipleDecrease(MultipleDecrease decrease) {
		// TODO Auto-generated method stub
		
	}

}
