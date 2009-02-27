package com.knitml.renderer.impl.charting;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.Stack;
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
import com.knitml.engine.settings.Direction;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.Renderer;
import com.knitml.renderer.context.RenderingContext;

public class ChartingRenderer implements Renderer {

	private Writer writer;
	private int maxStitchesToChart;
	private KnittingShape shape = KnittingShape.FLAT;
	private Direction direction = Direction.FORWARDS;
	private List<List<ChartElement>> chart = new ArrayList<List<ChartElement>>();
	private List<ChartElement> currentRow;
	private Stack<RepeatSet> repeatSetStack = new Stack<RepeatSet>();

	private RenderingContext renderingContext;

	public void setMaxStitchesToChart(int maxStitchesToChart) {
		this.maxStitchesToChart = maxStitchesToChart;
	}
	
	protected List<List<ChartElement>> getChart() {
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

	public void beginInstructionDefinition(Instruction instruction, String label) {
		this.direction = Direction.FORWARDS;
		// the engine hasn't been used yet to set shape, so get this information
		// from the instruction itself
		this.shape = instruction.getKnittingShape();
		chart = new ArrayList<List<ChartElement>>();
	}

	public void endInstructionDefinition() {
		endInstruction();
	}

	public void beginInstruction(Instruction instruction, String label) {
		this.direction = Direction.FORWARDS;
		// during directions, trust that the engine is right
		this.shape = renderingContext.getEngine().getKnittingShape();
		chart = new ArrayList<List<ChartElement>>();
	}

	public void endInstruction() {
		// TODO write out the pattern somehow
	}

	public void beginInlineInstruction(InlineInstruction instruction) {
		// TODO Auto-generated method stub

	}

	public void endInlineInstruction(InlineInstruction instruction) {
		// TODO Auto-generated method stub
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
		if (point == ChartElement.K) {
			return ChartElement.P;
		} else if (point == ChartElement.P) {
			return ChartElement.K;
		} else if (point == ChartElement.K2TOG) {
			return ChartElement.SSK;
		} else if (point == ChartElement.SSK) {
			return ChartElement.K2TOG;
		} else {
			return point;
		}
	}

	public void beginRow() {
		this.currentRow = new ArrayList<ChartElement>(this.maxStitchesToChart);
		chart.add(currentRow);
	}

	public void endRow(Row row, KnittingShape doNotUseThisVariable) {
		currentRow = null;
		if (shape == KnittingShape.FLAT) {
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
			switch (decrease.getType()) {
			case K2TOG:
				point = ChartElement.K2TOG;
				break;
			case SSK:
				point = ChartElement.SSK;
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

	public void renderKnit(Knit knit) {
		int times = knit.getNumberOfTimes() == null ? 1 : knit
				.getNumberOfTimes();
		for (int i = 0; i < times; i++) {
			add(ChartElement.K);
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
		int times = purl.getNumberOfTimes() == null ? 1 : purl
				.getNumberOfTimes();
		for (int i = 0; i < times; i++) {
			add(ChartElement.P);
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
		if (increase.getType() != null) {
			switch (increase.getType()) {
			case YO:
				point = ChartElement.YO;
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

	public boolean renderInlineInstructionRef(
			InlineInstructionRef instructionRef, String label) {
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

	public Instruction evaluateInstruction(Instruction instruction) {
		throw new NotImplementedException();
	}

	public Instruction evaluateInstructionDefinition(Instruction instruction) {
		throw new NotImplementedException();
	}

}
