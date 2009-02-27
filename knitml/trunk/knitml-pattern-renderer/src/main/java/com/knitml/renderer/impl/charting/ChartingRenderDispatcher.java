package com.knitml.renderer.impl.charting;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.knitml.renderer.impl.charting.analyzer.Analysis;
import com.knitml.renderer.impl.charting.analyzer.ChartingAnalyzer;

public class ChartingRenderDispatcher implements Renderer {

	private Renderer delegate;
	private Renderer fallbackRenderer;
	private ChartingRenderer chartingRenderer;
	private ChartingAnalyzer analyzer;
	private boolean charting = false;
	private Map<String, Analysis> instructionIdToAnalysisMap = new HashMap<String, Analysis>();

	public ChartingRenderDispatcher(Renderer fallbackRenderer) {
		this.fallbackRenderer = fallbackRenderer;
		this.delegate = fallbackRenderer;
		this.chartingRenderer = new ChartingRenderer();
	}

	public void setRenderingContext(RenderingContext renderingContext) {
		chartingRenderer.setRenderingContext(renderingContext);
		fallbackRenderer.setRenderingContext(renderingContext);
		analyzer = new ChartingAnalyzer(renderingContext);
	}

	public void setWriter(Writer writer) {
		chartingRenderer.setWriter(writer);
		fallbackRenderer.setWriter(writer);
	}

	protected boolean isCharting() {
		return charting;
	}
	
	protected List<List<ChartElement>> getChart() {
		return chartingRenderer.getChart();
	}

	protected void beginCharting(Analysis analysis) {
		this.chartingRenderer.setMaxStitchesToChart(analysis.getMaxWidth());
		this.delegate = this.chartingRenderer;
		this.charting = true;
	}

	protected void endCharting() {
		this.delegate = this.fallbackRenderer;
		this.charting = false;
	}
	
	public Instruction evaluateInstruction(Instruction instruction) {
		return doEvaluateInstruction(instruction, false);
	}

	public Instruction evaluateInstructionDefinition(Instruction instruction) {
		return doEvaluateInstruction(instruction, true);
	}

	protected Instruction doEvaluateInstruction(Instruction instruction,
			boolean definitionOnly) {
		Analysis analysis = analyzer.analyzeInstruction(instruction,
				definitionOnly);
		if (analysis.isChartable()) { 
			instructionIdToAnalysisMap.put(instruction.getId(), analysis);
			return analysis.getInstructionToUse();
		} else { // null means we could not chart
			return null;
		}
	}

	public void beginInstruction(Instruction instruction, String label) {
		Analysis analysis = instructionIdToAnalysisMap.get(instruction.getId());
		if (analysis != null) {
			beginCharting(analysis);
		}
		delegate.beginInstruction(instruction, label);
	}

	public void beginInstructionDefinition(Instruction instruction, String label) {
		Analysis analysis = instructionIdToAnalysisMap.get(instruction.getId());
		if (analysis != null) {
			beginCharting(analysis);
		}
		delegate.beginInstructionDefinition(instruction, label);
	}

	public void endInstruction() {
		delegate.endInstruction();
		if (isCharting()) {
			endCharting();
		}
	}

	public void endInstructionDefinition() {
		delegate.endInstructionDefinition();
		if (isCharting()) {
			endCharting();
		}
	}

	// delegate-only methods

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

	public void endInlineInstructionDefinition(InlineInstruction instruction) {
		delegate.endInlineInstructionDefinition(instruction);
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

	public void renderBindOff(BindOff bindOff) {
		delegate.renderBindOff(bindOff);
	}

	public void renderBindOffAll(BindOffAll bindOff) {
		delegate.renderBindOffAll(bindOff);
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

	public void beginInlineInstruction(InlineInstruction instruction) {
		delegate.beginInlineInstruction(instruction);
	}

	public void beginRepeat(Repeat repeat) {
		delegate.beginRepeat(repeat);
	}

	public void beginRow() {
		delegate.beginRow();
	}

	public void endInlineInstruction(InlineInstruction instruction) {
		delegate.endInlineInstruction(instruction);
	}

	public void endRepeat(Until until, Integer value) {
		delegate.endRepeat(until, value);
	}

	public void endRow(Row row, KnittingShape shape) {
		delegate.endRow(row, shape);
	}

	public void renderDecrease(Decrease decrease) {
		delegate.renderDecrease(decrease);
	}

	public void renderIncrease(Increase increase) {
		delegate.renderIncrease(increase);
	}

	public boolean renderInlineInstructionRef(
			InlineInstructionRef instructionRef, String label) {
		return delegate.renderInlineInstructionRef(instructionRef, label);
	}

	public void renderKnit(Knit knit) {
		delegate.renderKnit(knit);
	}

	public void renderPurl(Purl purl) {
		delegate.renderPurl(purl);
	}

	public void renderSlip(Slip slip) {
		delegate.renderSlip(slip);
	}

}
