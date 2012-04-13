package com.knitml.renderer.impl.charting;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import com.google.inject.assistedinject.Assisted;
import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.Side;
import com.knitml.core.model.common.Needle;
import com.knitml.core.model.common.StitchesOnNeedle;
import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.operations.DiscreteInlineOperation;
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
import com.knitml.renderer.BaseRendererFactory;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.Options;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.impl.charting.analyzer.Analysis;
import com.knitml.renderer.impl.charting.analyzer.ChartingAnalyzer;

public class ChartingRenderer implements Renderer {

	// properties injected through the constructor
	@Inject
	private Provider<ChartProducer> chartProducerProvider;

	// properties assisted through the factory (also from constructor)
	private RenderingContext renderingContext;

	// initialized once
	private ChartingAnalyzer analyzer;
	private Renderer fallbackRenderer;
	private InstructionInfo currentChartingInstruction = null;
	private Map<String, Analysis> instructionIdToAnalysisMap = new HashMap<String, Analysis>();
	
	// initialized per chart
	private Renderer delegate;
	private ChartProducer chartProducer;
	private StringWriter chartOutput;

	@Inject
	public ChartingRenderer(
			@Assisted RenderingContext renderingContext,
			@Assisted Writer writer,
			BaseRendererFactory baseRendererFactory,
			Provider<ChartProducer> chartProducerProvider) {
		this.renderingContext = renderingContext;
		this.analyzer = new ChartingAnalyzer(renderingContext);
		this.fallbackRenderer = baseRendererFactory.create(renderingContext, writer);
		// the delegate starts off as the non-charting renderer
		this.delegate = fallbackRenderer;
		this.chartProducerProvider = chartProducerProvider;
	}

	protected boolean isCharting() {
		return currentChartingInstruction != null;
	}

	protected List<List<ChartElement>> getGraph() {
		return chartProducer.getChart().getGraph();
	}

	protected Map<ChartElement, Map<Side,DiscreteInlineOperation>> getLegend() {
		return chartProducer.getChart().getLegend();
	}

	protected void beginCharting(InstructionInfo instructionInfo,
			Analysis analysis) {
		chartProducer = chartProducerProvider.get();
		chartProducer.setRenderingContext(renderingContext);
		chartProducer.setAnalysis(analysis);
		chartOutput = new StringWriter(512);
		chartProducer.setWriter(chartOutput);
		delegate = chartProducer;
		currentChartingInstruction = instructionInfo;
	}

	protected void endCharting() {
		delegate = fallbackRenderer;
		chartProducer.setWriter(null);
		chartProducer.setAnalysis(null);
		currentChartingInstruction = null;
	}

	public Instruction evaluateInstruction(Instruction instruction,
			RepeatInstruction repeatInstruction) {
		return doEvaluateInstruction(instruction, repeatInstruction, false);
	}

	public Instruction evaluateInstructionDefinition(Instruction instruction) {
		return doEvaluateInstruction(instruction, null, true);
	}

	protected Instruction doEvaluateInstruction(Instruction instruction,
			RepeatInstruction repeatInstruction, boolean definitionOnly) {
		Options options = renderingContext.getOptions();
		if (options.shouldChart(instruction)) {
			Analysis analysis = analyzer.analyzeInstruction(instruction,
					repeatInstruction, definitionOnly);
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
			InstructionInfo instructionInfo = currentChartingInstruction;
			instructionInfo.setRenderedText(chartOutput.toString());
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
			InstructionInfo instructionInfo = currentChartingInstruction;
			instructionInfo.setRenderedText(chartOutput.toString());
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

	public void renderCastOn(InlineCastOn castOn) {
		delegate.renderCastOn(castOn);
	}

	public void renderPickUpStitches(PickUpStitches pickUpStitches) {
		delegate.renderPickUpStitches(pickUpStitches);
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

	public void renderNoStitch(NoStitch noStitch) {
		delegate.renderNoStitch(noStitch);
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

	public void beginFromStitchHolder(FromStitchHolder fromStitchHolder) {
		delegate.beginFromStitchHolder(fromStitchHolder);
	}

	public void endFromStitchHolder(FromStitchHolder fromStitchHolder) {
		delegate.endFromStitchHolder(fromStitchHolder);
	}

	public void renderSlipToStitchHolder(SlipToStitchHolder operation) {
		delegate.renderSlipToStitchHolder(operation);
	}

	public void renderWorkEven(WorkEven operation) {
		delegate.renderWorkEven(operation);
	}

	public RenderingContext getRenderingContext() {
		return this.renderingContext;
	}

	public boolean beginIncreaseIntoNextStitch(
			IncreaseIntoNextStitch increaseIntoNextStitch) {
		return delegate.beginIncreaseIntoNextStitch(increaseIntoNextStitch);
	}

	public void endIncreaseIntoNextStitch(
			IncreaseIntoNextStitch increaseIntoNextStitch) {
		delegate.endIncreaseIntoNextStitch(increaseIntoNextStitch);
	}

	public void renderPassPreviousStitchOver(PassPreviousStitchOver ppso) {
		delegate.renderPassPreviousStitchOver(ppso);
	}

	public boolean beginOperationGroup(OperationGroup group) {
		return delegate.beginOperationGroup(group);
	}

	public void endOperationGroup(OperationGroup group) {
		delegate.endOperationGroup(group);
	}

	public void renderMultipleDecrease(MultipleDecrease decrease) {
		delegate.renderMultipleDecrease(decrease);
	}
	
}
