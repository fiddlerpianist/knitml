package com.knitml.renderer.impl.charting;

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
import com.knitml.core.model.directions.inline.InlinePickUpStitches;
import com.knitml.core.model.directions.inline.Knit;
import com.knitml.core.model.directions.inline.Purl;
import com.knitml.core.model.directions.inline.Repeat;
import com.knitml.core.model.directions.inline.Slip;
import com.knitml.core.model.directions.inline.Repeat.Until;
import com.knitml.core.model.header.GeneralInformation;
import com.knitml.core.model.header.Needle;
import com.knitml.core.model.header.Supplies;
import com.knitml.core.model.header.Yarn;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.Renderer;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.impl.charting.analyzer.ChartingAnalyzer;

public class ChartingRenderer implements Renderer {

	private RenderingContext renderingContext;
	private Renderer delegate;
	
	public ChartingRenderer(Renderer fallbackRenderer) {
		this.delegate = fallbackRenderer;
	}

	private ChartingHelper helper = new ChartingHelper();

	protected ChartingHelper getChartingHelper() {
		return helper;
	}

	private boolean charting = false;

	protected boolean isCurrentlyCharting() {
		return charting;
	}

	public Instruction evaluateInstructionDefinition(Instruction instruction) {
		return doEvaluateInstruction(instruction, true);
	}
	
	public Instruction evaluateInstruction(Instruction instruction) {
		return doEvaluateInstruction(instruction, false);
	}
	
	protected Instruction doEvaluateInstruction(Instruction instruction, boolean definitionOnly) {
		Object engineState = renderingContext.getEngine().save();
		ChartingAnalyzer analyzer = new ChartingAnalyzer(renderingContext, definitionOnly);

		// Analyze the instruction to see if we can chart it
		Instruction newInstruction = analyzer.analyzeInstruction(instruction);
		// set the engine back to original state
		renderingContext.getEngine().restore(engineState);

		if (newInstruction == null) { // this means we could not chart
			return instruction;
		} else {
			this.charting = true;
			return newInstruction;
		}
	}

	public void beginInstruction(Instruction instruction, String label) {
	}

	public void renderBindOff(BindOff bindOff) {
		delegate.renderBindOff(bindOff);
	}

	public void renderBindOffAll(BindOffAll bindOff) {
		delegate.renderBindOffAll(bindOff);
		// TODO bindOffAll on engine
	}

	public void renderCastOn(CastOn castOn) {
		delegate.renderCastOn(castOn);
	}

	public void renderPickUpStitches(InlinePickUpStitches pickUpStitches) {
		delegate.renderPickUpStitches(pickUpStitches);
	}

	public void renderDeclareFlatKnitting(DeclareFlatKnitting spec) {
		delegate.renderDeclareFlatKnitting(spec);
	}

	public void renderDeclareRoundKnitting() {
		delegate.renderDeclareRoundKnitting();
	}

	public void renderPlaceMarker() {
		delegate.renderPlaceMarker();
	}

	public void renderRemoveMarker() {
		delegate.renderRemoveMarker();
	}

	public void beginRepeat(Repeat repeat) {
		if (!isCurrentlyCharting()) {
			delegate.beginRepeat(repeat);
		}
	}

	public void endRepeat(Until until, Integer value) {
		if (isCurrentlyCharting()) {
			getChartingHelper().endRepeat();
		} else {
			delegate.endRepeat(until, value);
		}
	}

	public void beginRow(Row row, KnittingShape shape) {
		if (isCurrentlyCharting()) {
			getChartingHelper().beginRow(row);
		} else {
			delegate.beginRow(row, shape);
		}
	}

	public void endInstruction() {
		if (isCurrentlyCharting()) {
			getChartingHelper().endChart();
			charting = false;
		} else {
			delegate.endInstruction();
		}
	}

	public void endRow() {
		if (isCurrentlyCharting()) {
			getChartingHelper().endRow();
		} else {
			delegate.endRow();
		}
	}

	public void renderDecrease(Decrease decrease) {
		if (isCurrentlyCharting()) {
			getChartingHelper().decrease(decrease);
		} else {
			delegate.renderDecrease(decrease);
		}
	}

	public void renderIncrease(Increase increase) {
		if (isCurrentlyCharting()) {
			getChartingHelper().increase(increase);
		} else {
			delegate.renderIncrease(increase);
		}
	}

	public boolean renderInlineInstructionRef(
			InlineInstructionRef instructionRef, String label) {
		if (isCurrentlyCharting()) {
			// If charting, don't render it. This will make the controller walk
			// through the children
			return false;
		} else {
			return delegate.renderInlineInstructionRef(instructionRef, label);
		}
	}

	public void renderKnit(Knit knit) {
		if (isCurrentlyCharting()) {
			getChartingHelper().knit(knit);
		} else {
			delegate.renderKnit(knit);
		}
	}

	public void renderPurl(Purl purl) {
		if (isCurrentlyCharting()) {
			getChartingHelper().purl(purl);
		} else {
			delegate.renderPurl(purl);
		}
	}

	public void renderSlip(Slip slip) {
		if (isCurrentlyCharting()) {
			getChartingHelper().slip(slip);
		} else {
			delegate.renderSlip(slip);
		}
	}

	// delegate-only methods

	public void beginInstructionDefinition(Instruction instruction, String label) {
		delegate.beginInstructionDefinition(instruction, label);
	}

	public void addYarn(Yarn yarn) {
		delegate.addYarn(yarn);
	}

	public void beginDirections() {
		delegate.beginDirections();
	}

	public void beginInformation() {
		delegate.beginInformation();
	}

	public void beginInlineInstructionDefinition(InlineInstruction instruction,
			String label) {
		delegate.beginInlineInstructionDefinition(instruction, label);
	}

	public void beginInstructionDefinitions() {
		delegate.beginInstructionDefinitions();
	}

	public void beginInstructionGroup() {
		delegate.beginInstructionGroup();
	}

	public void beginInstructionGroup(String label) {
		delegate.beginInstructionGroup(label);
	}

	public void beginSection(Section section) {
		delegate.beginSection(section);
	}

	public void beginUsingNeedle(Needle needle) {
		delegate.beginUsingNeedle(needle);
	}

	public void endDirections() {
		delegate.endDirections();
	}

	public void endInformation() {
		delegate.endInformation();
	}

	public void endInlineInstructionDefinition() {
		delegate.endInlineInstructionDefinition();
	}

	public void endInstructionDefinition() {
		delegate.endInstructionDefinition();
	}

	public void endInstructionDefinitions() {
		delegate.endInstructionDefinitions();
	}

	public void endInstructionGroup() {
		delegate.endInstructionGroup();
	}

	public void endSection() {
		delegate.endSection();
	}

	public void endUsingNeedle() {
		delegate.endUsingNeedle();
	}

	public void renderApplyNextRow(ApplyNextRow applyNextRow, String label) {
		delegate.renderApplyNextRow(applyNextRow, label);
	}

	public void renderArrangeStitchesOnNeedles(List<StitchesOnNeedle> needles) {
		delegate.renderArrangeStitchesOnNeedles(needles);
	}

	public void renderCrossStitches(CrossStitches element) {
		delegate.renderCrossStitches(element);
	}

	public void renderDesignateEndOfRow(KnittingShape currentKnittingShape) {
		delegate.renderDesignateEndOfRow(currentKnittingShape);
	}

	public void renderGeneralInformation(GeneralInformation generalInformation) {
		delegate.renderGeneralInformation(generalInformation);
	}

	public void renderGraftStitchesTogether(List<Needle> needles) {
		delegate.renderGraftStitchesTogether(needles);
	}

	public void renderJoinInRound() {
		delegate.renderJoinInRound();
	}

	public void renderMessage(String messageToRender) {
		delegate.renderMessage(messageToRender);
	}

	public void renderNumberOfStitchesInRow(NumberOfStitches numberOfStitches) {
		delegate.renderNumberOfStitchesInRow(numberOfStitches);
	}

	public void renderRepeatInstruction(RepeatInstruction repeatInstruction,
			InstructionInfo instructionInfo) {
		delegate.renderRepeatInstruction(repeatInstruction, instructionInfo);
	}

	public void renderSupplies(Supplies supplies) {
		delegate.renderSupplies(supplies);
	}

	public void renderTurn() {
		delegate.renderTurn();
	}

	public void renderUnworkedStitches(int number) {
		delegate.renderUnworkedStitches(number);
	}

	public void renderUseNeedles(List<Needle> needless) {
		delegate.renderUseNeedles(needless);
	}

	public void renderUsingNeedlesCastOn(List<Needle> needles, CastOn castOn) {
		delegate.renderUsingNeedlesCastOn(needles, castOn);
	}

	public void setRenderingContext(RenderingContext renderingContext) {
		this.renderingContext = renderingContext;
		delegate.setRenderingContext(renderingContext);
	}

	public void setWriter(Writer writer) {
		delegate.setWriter(writer);
	}

	public boolean requiresExplicitInlineInstructions() {
		if (isCurrentlyCharting()) {
			return false;
		} else {
			// FIXME should be: delegate.requiresExplicitInlineInstructions();
			return false;
		}
	}

	public boolean requiresExplicitRepeats() {
		if (isCurrentlyCharting()) {
			return true;
		} else {
			return delegate.requiresExplicitRepeats();
		}
	}

}
