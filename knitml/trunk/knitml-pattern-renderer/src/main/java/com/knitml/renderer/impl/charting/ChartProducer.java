package com.knitml.renderer.impl.charting;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;
import org.apache.commons.lang.NotImplementedException;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.LoopToWork;
import com.knitml.core.common.Side;
import com.knitml.core.common.Stack;
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
import com.knitml.core.model.directions.inline.Repeat.Until;
import com.knitml.core.model.header.GeneralInformation;
import com.knitml.core.model.header.Needle;
import com.knitml.core.model.header.Supplies;
import com.knitml.core.model.header.Yarn;
import com.knitml.engine.settings.Direction;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.chart.Chart;
import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.symbol.SymbolProvider;
import com.knitml.renderer.chart.symbol.SymbolProviderRegistry;
import com.knitml.renderer.chart.writer.ChartWriter;
import com.knitml.renderer.chart.writer.ChartWriterFactory;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.impl.charting.analyzer.Analysis;
import com.knitml.renderer.impl.charting.analyzer.ChartingAnalyzer;
import com.knitml.renderer.impl.charting.analyzer.RowInfo;

/**
 * A renderer which performs the layout of ChartElements in logical form, based
 * on the Instruction passed to it. Not meant to be used as a standalone
 * Renderer implementation, as many methods in this class are unimplemented.
 * 
 * @see ChartingAnalyzer
 * @see ChartingRenderer
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
class ChartProducer implements Renderer {

	private static BidiMap<ChartElement, ChartElement> symbolInverseMap = new DualHashBidiMap<ChartElement, ChartElement>();

	static {
		symbolInverseMap.put(ChartElement.P, ChartElement.K);
		symbolInverseMap.put(ChartElement.P_TW, ChartElement.K_TW);
		symbolInverseMap.put(ChartElement.P2TOG, ChartElement.K2TOG);
		symbolInverseMap.put(ChartElement.P2TOG_TBL, ChartElement.K2TOG_TBL);
		symbolInverseMap.put(ChartElement.SSP, ChartElement.SSK);
		symbolInverseMap.put(ChartElement.SKP, ChartElement.SSP);
	}

	// fields which should be set before calling begin() methods
	private ChartWriterFactory chartWriterFactory;
	private SymbolProviderRegistry registry;
	private Writer writer;
	private Analysis analysis;

	private Direction direction = Direction.FORWARDS;
	private Chart chart;
	private List<ChartElement> currentRow;
	private Iterator<RowInfo> rowInfoIterator;
	private RowInfo currentRowInfo;

	@SuppressWarnings("unused")
	private boolean condenseRepeats = true; // assume that we can until we can't
	private Stack<RepeatSet> repeatSetStack = new Stack<RepeatSet>();

	private RenderingContext renderingContext;

	public ChartProducer(ChartWriterFactory chartWriterFactory,
			SymbolProviderRegistry registry) {
		this.chartWriterFactory = chartWriterFactory;
		this.registry = registry;
	}

	public void setAnalysis(Analysis analysis) {
		this.analysis = analysis;
		if (analysis != null) {
			for (RowInfo rowInfo : analysis.getRowInfos()) {
				if (rowInfo.getRowWidth() != analysis.getMaxWidth()) {
					this.condenseRepeats = false;
					break;
				}
			}
		}
	}

	protected Chart getChart() {
		return chart;
	}

	// methods on repeatSetStack

	public boolean isWithinRepeatSet() {
		return !repeatSetStack.empty();
	}

	public RepeatSet getCurrentRepeatSet() {
		return repeatSetStack.peek();
	}

	public void addNewRepeatSet(RepeatSet newRepeatSet) {
		if (isWithinRepeatSet()) {
			getCurrentRepeatSet().addOperation(newRepeatSet);
		}
		repeatSetStack.push(newRepeatSet);
	}

	public RepeatSet removeCurrentRepeatSet() {
		return repeatSetStack.pop();
	}

	// methods from Renderer that are implemented

	public void beginInstructionDefinition(InstructionInfo instructionInfo) {
		if (this.analysis.getStartingSide() == Side.WRONG) {
			this.direction = Direction.BACKWARDS;
		} else {
			this.direction = Direction.FORWARDS;
		}
		// the engine hasn't been used yet to set shape, so get this information
		// from the instruction itself
		chart = new Chart();
		chart.setTitle(instructionInfo.getLabel());
		chart.setShape(instructionInfo.getInstruction().getKnittingShape());
		chart.setWidth(analysis.getMaxWidth());
		this.rowInfoIterator = analysis.getRowInfos().iterator();
	}

	public void endInstructionDefinition() {
		endInstruction();
	}

	public void beginInstruction(InstructionInfo instructionInfo) {
		chart = new Chart();
		if (this.analysis.getStartingSide() == Side.WRONG) {
			this.direction = Direction.BACKWARDS;
			chart.setStartingSide(Side.WRONG);
		} else {
			this.direction = Direction.FORWARDS;
			chart.setStartingSide(Side.RIGHT);
		}
		// FIXME internationalize
		chart.setTitle("Work as follows");
		// during directions, trust that the engine is right
		chart.setShape(renderingContext.getEngine().getKnittingShape());
		chart.setWidth(analysis.getMaxWidth());
		chart.setStartingRowNumber(instructionInfo.getRowRange()
				.getMinimumInteger());
		this.rowInfoIterator = analysis.getRowInfos().iterator();
	}

	public void endInstruction() {
		// TODO pass an ID to use somehow
		SymbolProvider translator = registry.getSymbolProvider(null);
		ChartWriter writer = chartWriterFactory.createChartWriter(translator);
		writer.writeChart(chart, this.writer);
	}

	public void beginInlineInstruction(InlineInstruction instruction) {
		// Do nothing, as the children will get visited
	}

	public void endInlineInstruction(InlineInstruction instruction) {
		// Do nothing, as the children will get visited
	}

	protected void add(ChartElement point) {
		if (isWithinRepeatSet()) {
			getCurrentRepeatSet().addOperation(point);
		} else {
			if (direction == Direction.BACKWARDS) {
				currentRow.add(0, inverse(point));
			} else {
				currentRow.add(point);
			}
		}
	}

	protected ChartElement inverse(ChartElement point) {
		ChartElement element = symbolInverseMap.get(point);
		if (element == null) {
			element = symbolInverseMap.inverseBidiMap().get(point);
		}
		if (element == null) {
			element = point;
		}
		return element;
	}

	public void beginRow() {
		this.currentRow = new ArrayList<ChartElement>(analysis.getMaxWidth());
		this.currentRowInfo = rowInfoIterator.next();
		chart.addRow(currentRow);
	}

	public void endRow(Row row, KnittingShape doNotUseThisVariable) {
		// pad whatever is left with no-stitch elements
		for (int i = currentRowInfo.getRowWidth(); i < analysis.getMaxWidth(); i++) {
			currentRow.add(ChartElement.NS);
		}

		// release currentRow... it has already been added to the chart in
		// beginRow()
		currentRow = null;
		if (chart.getShape() == KnittingShape.FLAT) {
			if (direction == Direction.FORWARDS) {
				direction = Direction.BACKWARDS;
			} else {
				direction = Direction.FORWARDS;
			}
		}
	}

	public void beginRepeat(Repeat repeat) {
		RepeatSet repeatSet = new RepeatSet(repeat);
		addNewRepeatSet(repeatSet);
	}

	public void endRepeat(Until until, Integer value) {
		RepeatSet repeatSet = removeCurrentRepeatSet();
		if (!isWithinRepeatSet()) { // i.e. the repeatSetStack is empty
			addRepeatSetToRow(repeatSet);
		}
	}

	protected void addRepeatSetToRow(RepeatSet repeatSet) {
		if (repeatSet.getRepeat().getUntil() != Until.TIMES) {
			throw new IllegalArgumentException(
					"All repeats handed to this renderer must use the TIMES type of until attribute");
		}
		int times = repeatSet.getRepeat().getValue();
		for (int i = 0; i < times; i++) {
			// work repeat set
			for (Object operation : repeatSet) {
				if (operation instanceof ChartElement) {
					add((ChartElement) operation);
				} else {
					// recurse through the nested repeat
					addRepeatSetToRow((RepeatSet) operation);
				}
			}
		}
	}

	public void renderCrossStitches(CrossStitches element) {
		// TODO Auto-generated method stub

	}

	public void renderDecrease(Decrease decrease) {
		int times = decrease.getNumberOfTimes() == null ? 1 : decrease
				.getNumberOfTimes();
		ChartElement point = null;
		if (decrease.getType() != null) {
			try {
				point = ChartElement.valueOf(decrease.getType().name());
			} catch (IllegalArgumentException ex) {
				throw new RuntimeException(
						"This type of ChartElement is not defined yet. If the pattern validates against the schema, please report this as a bug",
						ex);
			}
		} else {
			point = ChartElement.DECREASE;
		}
		for (int i = 0; i < times; i++) {
			add(point);
		}
	}

	public void renderKnit(Knit knit) {
		ChartElement chartElement = ChartElement.K;
		if (knit.getLoopToWork() == LoopToWork.TRAILING) {
			chartElement = ChartElement.K_TW;
		}
		int times = knit.getNumberOfTimes() == null ? 1 : knit
				.getNumberOfTimes();
		for (int i = 0; i < times; i++) {
			add(chartElement);
		}
	}

	public void renderNoStitch(NoStitch noStitch) {
		int times = noStitch.getNumberOfStitches() == null ? 1 : noStitch
				.getNumberOfStitches();
		for (int i = 0; i < times; i++) {
			add(ChartElement.NS);
		}
	}

	public void renderNumberOfStitchesInRow(NumberOfStitches numberOfStitches) {
		// information only. It will be evident from the chart.
		return;
	}

	public void renderPlaceMarker() {
		return;
	}

	public void renderPurl(Purl purl) {
		ChartElement chartElement = ChartElement.P;
		if (purl.getLoopToWork() == LoopToWork.TRAILING) {
			chartElement = ChartElement.P_TW;
		}
		int times = purl.getNumberOfTimes() == null ? 1 : purl
				.getNumberOfTimes();
		for (int i = 0; i < times; i++) {
			add(chartElement);
		}
	}

	public void renderRemoveMarker() {
		return;
	}

	public void renderSlip(Slip slip) {
		int times = slip.getNumberOfTimes() == null ? 1 : slip
				.getNumberOfTimes();
		for (int i = 0; i < times; i++) {
			add(ChartElement.SL);
		}
	}

	public void renderIncrease(Increase increase) {
		int times = increase.getNumberOfTimes() == null ? 1 : increase
				.getNumberOfTimes();
		ChartElement point = null;
		if (increase.getType() == null) {
			point = ChartElement.M1;
		} else {
			switch (increase.getType()) {
			case YO:
				point = ChartElement.YO;
				break;
			case M1P:
				point = ChartElement.M1P;
				break;
			default:
				throw new RuntimeException(
						"This type of ChartElement is not defined yet (testing purposes only)");
			}
		}
		for (int i = 0; i < times; i++) {
			add(point);
		}
	}

	public void renderInlineInstructionRef(InlineInstructionRef instructionRef,
			String label) {
		// FIXME make this go
		throw new NotImplementedException("But it will be soon!");
	}

	public void setRenderingContext(RenderingContext renderingContext) {
		this.renderingContext = renderingContext;
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
	}

	// all un-implemented methods

	public void addYarn(Yarn yarn) {
		throw new NotImplementedException();
	}

	public void beginDirections() {
		throw new NotImplementedException();
	}

	public void beginInformation() {
		throw new NotImplementedException();
	}

	public void beginInlineInstructionDefinition(InlineInstruction instruction,
			String label) {
		throw new NotImplementedException();
	}

	public void beginInstructionDefinitions() {
		throw new NotImplementedException();
	}

	public void beginInstructionGroup(String label) {
		throw new NotImplementedException();
	}

	public void beginInstructionGroup() {
		throw new NotImplementedException();
	}

	public void beginSection(Section section) {
		throw new NotImplementedException();
	}

	public void beginUsingNeedle(Needle needle) {
		throw new NotImplementedException();
	}

	public void endDirections() {
		throw new NotImplementedException();
	}

	public void endInformation() {
		throw new NotImplementedException();
	}

	public void endInlineInstructionDefinition(InlineInstruction instruction) {
		throw new NotImplementedException();
	}

	public void endInstructionDefinitions() {
		throw new NotImplementedException();
	}

	public void endInstructionGroup() {
		throw new NotImplementedException();
	}

	public void endSection() {
		throw new NotImplementedException();
	}

	public void endUsingNeedle() {
		throw new NotImplementedException();
	}

	public void renderApplyNextRow(ApplyNextRow applyNextRow, String label) {
		// for now, at least
		throw new NotImplementedException();
	}

	public void renderArrangeStitchesOnNeedles(List<StitchesOnNeedle> needles) {
		throw new NotImplementedException();
	}

	public void renderBindOff(BindOff bindOff) {
		throw new NotImplementedException();
	}

	public void renderBindOffAll(BindOffAll bindOff) {
		throw new NotImplementedException();
	}

	public void renderCastOn(CastOn castOn) {
		throw new NotImplementedException();
	}

	public void renderDeclareFlatKnitting(DeclareFlatKnitting spec) {
		throw new NotImplementedException();
	}

	public void renderDeclareRoundKnitting() {
		throw new NotImplementedException();
	}

	public void renderDesignateEndOfRow(KnittingShape currentKnittingShape) {
		throw new NotImplementedException();
	}

	public void renderGeneralInformation(GeneralInformation generalInformation) {
		throw new NotImplementedException();
	}

	public void renderGraftStitchesTogether(List<Needle> needles) {
		throw new NotImplementedException();
	}

	public void renderJoinInRound() {
		throw new NotImplementedException();
	}

	public void renderMessage(String messageToRender) {
		throw new NotImplementedException();
	}

	public void renderPickUpStitches(PickUpStitches pickUpStitches) {
		throw new NotImplementedException();
	}

	public void renderPickUpStitches(InlinePickUpStitches pickUpStitches) {
		throw new NotImplementedException();
	}

	public void renderRepeatInstruction(RepeatInstruction repeatInstruction,
			InstructionInfo instructionInfo) {
		throw new NotImplementedException();
	}

	public void renderSupplies(Supplies supplies) {
		throw new NotImplementedException();
	}

	public void renderTurn() {
		throw new NotImplementedException();
	}

	public void renderUnworkedStitches(int number) {
		throw new NotImplementedException();
	}

	public void renderUseNeedles(List<Needle> needles) {
		throw new NotImplementedException();
	}

	public void renderUsingNeedlesCastOn(List<Needle> needles, CastOn castOn) {
		throw new NotImplementedException();
	}

	public Instruction evaluateInstruction(Instruction instruction,
			RepeatInstruction associatedRepeatInstruction) {
		throw new NotImplementedException();
	}

	public Instruction evaluateInstructionDefinition(Instruction instruction) {
		throw new NotImplementedException();
	}

	public void beginPattern(Pattern pattern) {
		throw new NotImplementedException();
	}

	public void endPattern() {
		throw new NotImplementedException();
	}

	public void beginFromStitchHolder(FromStitchHolder fromStitchHolder) {
		throw new NotImplementedException();
	}

	public void endFromStitchHolder(FromStitchHolder fromStitchHolder) {
		throw new NotImplementedException();
	}

	public void renderSlipToStitchHolder(SlipToStitchHolder operation) {
		throw new NotImplementedException();
	}

	public void renderWorkEven(WorkEven operation) {
		throw new NotImplementedException();
	}

	public void beginIncreaseIntoNextStitch(
			IncreaseIntoNextStitch increaseIntoNextStitch) {
		throw new NotImplementedException();
	}

	public void endIncreaseIntoNextStitch(
			IncreaseIntoNextStitch increaseIntoNextStitch) {
		throw new NotImplementedException();
	}

	public void renderPassPreviousStitchOver(PassPreviousStitchOver ppso) {
		throw new NotImplementedException();
	}

	public RenderingContext getRenderingContext() {
		return renderingContext;
	}

}
