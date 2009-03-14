package com.knitml.renderer.impl.charting;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.StitchesOnNeedle;
import com.knitml.core.model.Pattern;
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
import com.knitml.renderer.Renderer;
import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.symboladvisor.ChartSymbolAdvisorRegistry;
import com.knitml.renderer.chart.writer.ChartWriterFactory;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.Options;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.impl.charting.analyzer.Analysis;
import com.knitml.renderer.impl.charting.analyzer.ChartingAnalyzer;

public class ChartingRenderer implements Renderer {

	// properties that can be set by the client
	private Renderer fallbackRenderer;
	private RenderingContext renderingContext;

	private InstructionInfo currentChartingInstruction = null;
	private ChartingAnalyzer analyzer;
	private Map<String, Analysis> instructionIdToAnalysisMap = new HashMap<String, Analysis>();
	private StringWriter chartOutput;
	private ChartProducer chartProducer;
	private Renderer delegate;

	public ChartingRenderer(Renderer fallbackRenderer, RenderingContext renderingContext,
			ChartWriterFactory chartWriterFactory,
			ChartSymbolAdvisorRegistry registry) {
		if (chartWriterFactory == null || fallbackRenderer == null) {
			throw new IllegalArgumentException(
					"The chartWriterFactory and fallbackRenderer parameters must be set");
		}
		
		this.fallbackRenderer = fallbackRenderer;
		// initially set the fallback renderer as the delegate
		this.delegate = fallbackRenderer;
		
		this.renderingContext = renderingContext;
		
		// derive the charting analyzer
		analyzer = new ChartingAnalyzer(renderingContext);
		// derive the chart producer
		chartProducer = new ChartProducer(chartWriterFactory, registry);
		chartProducer.setRenderingContext(renderingContext);
	}

	protected boolean isCharting() {
		return currentChartingInstruction != null;
	}

	protected List<List<ChartElement>> getGraph() {
		return chartProducer.getChart().getGraph();
	}

	protected void beginCharting(InstructionInfo instructionInfo,
			Analysis analysis) {
		this.chartProducer.setAnalysis(analysis);
		this.chartOutput = new StringWriter(512);
		this.chartProducer.setWriter(chartOutput);
		this.delegate = this.chartProducer;
		this.currentChartingInstruction = instructionInfo;
	}

	protected void endCharting() {
		this.delegate = this.fallbackRenderer;
		this.chartProducer.setWriter(null);
		this.chartProducer.setAnalysis(null);
		this.currentChartingInstruction = null;
	}

	public Instruction evaluateInstruction(Instruction instruction) {
		return doEvaluateInstruction(instruction, false);
	}

	public Instruction evaluateInstructionDefinition(Instruction instruction) {
		return doEvaluateInstruction(instruction, true);
	}

	protected Instruction doEvaluateInstruction(Instruction instruction,
			boolean definitionOnly) {
		Options options = renderingContext.getOptions();
		if (options.shouldChart(instruction)) {
			Analysis analysis = analyzer.analyzeInstruction(instruction,
					definitionOnly);
			if (analysis.isChartable()) {
				instructionIdToAnalysisMap.put(instruction.getId(), analysis);
				return analysis.getInstructionToUse();
			}
		}
		// null means we could not chart
		return null;
	}

	public void beginInstruction(InstructionInfo instructionInfo) {
		Analysis analysis = instructionIdToAnalysisMap.get(instructionInfo
				.getId());
		if (analysis != null) {
			beginCharting(instructionInfo, analysis);
		}
		delegate.beginInstruction(instructionInfo);
	}

	public void beginInstructionDefinition(InstructionInfo instructionInfo) {
		Analysis analysis = instructionIdToAnalysisMap.get(instructionInfo
				.getId());
		if (analysis != null) {
			// this sets the delegate to be the ChartProducer
			beginCharting(instructionInfo, analysis);
		}
		delegate.beginInstructionDefinition(instructionInfo);
	}

	public void endInstruction() {
		delegate.endInstruction();
		if (isCharting()) {
			// this means that the delegate is the ChartProducer, so
			// set the text to be rendered into the InstructionInfo object
			// and call begin() and end() on the fallback renderer
			InstructionInfo instructionInfo = this.currentChartingInstruction;
			instructionInfo.setRenderedText(this.chartOutput.toString());
			fallbackRenderer.beginInstruction(instructionInfo);
			fallbackRenderer.endInstruction();
			endCharting();
			// now delegate is the fallback renderer
		}
	}

	public void endInstructionDefinition() {
		delegate.endInstructionDefinition();
		if (isCharting()) {
			// this means that the delegate is the ChartProducer, so
			// set the text to be rendered into the InstructionInfo object
			// and call begin() and end() on the fallback renderer
			InstructionInfo instructionInfo = this.currentChartingInstruction;
			instructionInfo.setRenderedText(this.chartOutput.toString());
			fallbackRenderer.beginInstructionDefinition(instructionInfo);
			fallbackRenderer.endInstructionDefinition();
			endCharting();
			// now delegate is the fallback renderer
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

	public void renderInlineInstructionRef(InlineInstructionRef instructionRef,
			String label) {
		delegate.renderInlineInstructionRef(instructionRef, label);
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

	public void beginPattern(Pattern pattern) {
		delegate.beginPattern(pattern);
	}

	public void endPattern() {
		delegate.endPattern();
	}

	public RenderingContext getRenderingContext() {
		return this.renderingContext;
	}

}
