package com.knitml.renderer.impl.charting.analyzer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.Side;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.InlineOperation;
import com.knitml.core.model.operations.StitchNature;
import com.knitml.core.model.operations.StitchNatureProducer;
import com.knitml.core.model.operations.YarnReferenceHolder;
import com.knitml.core.model.operations.block.Instruction;
import com.knitml.core.model.operations.block.RepeatInstruction;
import com.knitml.core.model.operations.block.Row;
import com.knitml.core.model.operations.inline.ApplyNextRow;
import com.knitml.core.model.operations.inline.BindOffAll;
import com.knitml.core.model.operations.inline.DesignateEndOfRow;
import com.knitml.core.model.operations.inline.FromStitchHolder;
import com.knitml.core.model.operations.inline.Increase;
import com.knitml.core.model.operations.inline.IncreaseIntoNextStitch;
import com.knitml.core.model.operations.inline.InlineCastOn;
import com.knitml.core.model.operations.inline.InlineInstruction;
import com.knitml.core.model.operations.inline.InlineInstructionRef;
import com.knitml.core.model.operations.inline.Knit;
import com.knitml.core.model.operations.inline.NoStitch;
import com.knitml.core.model.operations.inline.OperationGroup;
import com.knitml.core.model.operations.inline.PlaceMarker;
import com.knitml.core.model.operations.inline.Purl;
import com.knitml.core.model.operations.inline.Repeat;
import com.knitml.core.model.operations.inline.Repeat.Until;
import com.knitml.core.model.operations.inline.SlipToStitchHolder;
import com.knitml.core.model.operations.inline.Turn;
import com.knitml.core.model.operations.inline.UsingNeedle;
import com.knitml.core.model.operations.inline.WorkEven;
import com.knitml.engine.KnittingEngine;
import com.knitml.engine.settings.Direction;
import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.chart.ChartElementFinder;
import com.knitml.renderer.context.RenderingContext;

/**
 * <p>
 * Provides analysis of an instruction to determine whether it is chartable.
 * Should be used in conjunction with a specific renderer capable of handling
 * instructions returned from the analysis.
 * 
 * <p>
 * Note: this class is <i>not</i> thread-safe.
 * 
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class ChartingAnalyzer {

	private final ChartElementFinder chartElementFinder = new ChartElementFinder();

	private RenderingContext renderingContext;
	/**
	 * Set this variable to true to indicate that the KnittingEngine is not
	 * immediately in a state where knitting can commence, i.e. a pattern is
	 * processing an instruction definition in the header rather than in the
	 * formal directions section of the pattern.
	 */
	private boolean dynamicFirstRowCastOn = false;
	private int maxWidth = 0;
	private boolean containsNoStitchOperations = false;
	private List<RowInfo> rowInfos = new ArrayList<RowInfo>();
	private RowInfo currentRowInfo;
	private Side startingSide = null;

	private boolean currentlyRepeating = false;

	private KnittingShape shape = KnittingShape.FLAT;
	private final static Logger log = LoggerFactory
			.getLogger(ChartingAnalyzer.class);

	public ChartingAnalyzer(RenderingContext context) {
		this.renderingContext = context;
	}

	public Analysis analyzeInstruction(Instruction originalInstruction,
			RepeatInstruction repeatInstruction,
			boolean fromInstructionDefinition) {
		this.maxWidth = 0;
		this.rowInfos.clear();
		this.currentRowInfo = null;
		this.currentlyRepeating = false;
		this.shape = KnittingShape.FLAT;

		Object engineState = renderingContext.getEngine().save();

		if (fromInstructionDefinition
				&& originalInstruction.getStartingStitchCount() != null) {
			renderingContext.getEngine().castOn(
					originalInstruction.getStartingStitchCount(), false);
		} else {
			this.dynamicFirstRowCastOn = fromInstructionDefinition;
		}

		int startingStitches = renderingContext.getEngine()
				.getTotalNumberOfStitchesInRow();

		try {
			if (!originalInstruction.hasRows()) {
				// cannot handle for-each-row-in-instruction yet. Possibly this
				// would be expanded ahead of time.
				log.info("Cannot handle an Instruction whose operations are not made up entirely of Rows");
				return new Analysis();
			}

			List<Row> newRows = analyzeInstructionOnce(originalInstruction);
			if (newRows == null) {
				return new Analysis();
			}

			// TODO RepeatInstruction analysis here....

			// FIXME for now, simply refuse to chart if the chart is unbalanced
			// and there is a known repeat-instruction
			if (!fromInstructionDefinition && repeatInstruction != null) {
				int endingStitches = renderingContext.getEngine()
						.getTotalNumberOfStitchesInRow();
				if (startingStitches != endingStitches) {
					return new Analysis();
				}
			}

			// if we do tack on rows as the result of a repeat-instruction,
			// make sure we give the new instruction an unclassified ID instead
			// of the original ID. We don't want someone else adding a
			// repeat-instruction to the original + repeat-instruction chart.

			// end RepeatInstruction analysis

			Instruction newInstruction = new Instruction(originalInstruction,
					newRows);

			Analysis result = new Analysis();
			result.setInstructionToUse(newInstruction);
			result.setChartable(true);
			result.setMaxWidth(this.maxWidth);
			result.setRowInfos(this.rowInfos);
			result.setContainsNoStitchOperations(this.containsNoStitchOperations);
			result.setKnittingShape(this.shape);
			result.setStartingSide(this.startingSide);
			return result;
		} finally {
			renderingContext.getEngine().restore(engineState);
		}
	}

	protected List<Row> analyzeInstructionOnce(Instruction originalInstruction) {
		List<Row> newRows = new ArrayList<Row>();
		for (Row originalRow : originalInstruction.getRows()) {
			Row newRow = analyzeRow(originalRow);
			// if null is returned, we cannot chart, so return null up the
			// chain
			if (newRow == null) {
				return null;
			}
			newRows.add(newRow);
		}
		return newRows;
	}

	protected Row analyzeRow(Row originalRow) {
		KnittingEngine engine = renderingContext.getEngine();
		// can't handle short rows
		if (originalRow.isShortRow()) {
			return null;
		}
		// can't handle colors (right now)
		if (originalRow.getYarnIdRef() != null) {
			return null;
		}
		
		List<InlineOperation> newOperations = new ArrayList<InlineOperation>();
		Row newRow = new Row(originalRow, newOperations);
		this.currentRowInfo = new RowInfo();

		// Begin the row for the engine
		if (dynamicFirstRowCastOn) {
			// if this is an instruction definition, infer the intended shape
			// from what is specified by the first row's shape.
			// If nothing is specified, flat knitting is assumed.
			if (originalRow.getType() != null) {
				this.shape = originalRow.getType();
			}
			// cast on one stitch so that we can start a new row below
			engine.castOn(1, false);
		}
		engine.startNewRow();
		if (this.startingSide == null) {
			this.startingSide = Side.RIGHT;
			if (dynamicFirstRowCastOn && originalRow.getSide() == Side.WRONG) {
				this.startingSide = Side.WRONG;
				engine.knit();
				engine.endRow();
				engine.startNewRow();
			} else if (engine.getDirection() == Direction.BACKWARDS) {
				this.startingSide = Side.WRONG;
			}
		}

		// Walk through all of the row's operations and see how it affects the
		// counts
		for (InlineOperation operation : originalRow.getOperations()) {
			InlineOperation newOperation = handle(operation);
			// if null is returned, we cannot chart, so return null up the chain
			if (newOperation == null) {
				return null;
			}
			newOperations.add(newOperation);
		}

		// End the row for the engine
		if (dynamicFirstRowCastOn) {
			// remove the "extra" stitch we created in order to start the row.
			engine.reverseSlip(1);
			// don't swallow the real stitch's nature
			StitchNature stitchNature = engine.peekAtNextStitch()
					.getCurrentNature();
			if (stitchNature == StitchNature.PURL) {
				engine.purlTwoTogether();
			} else {
				engine.knitTwoTogether();
			}
			// now pretend like all is normal
			dynamicFirstRowCastOn = false;
		}
		engine.endRow();

		// if this row pushed the bounds of the previously recorded maximum
		// width, record the new max width
		if (currentRowInfo.getRowWidth() > maxWidth) {
			maxWidth = currentRowInfo.getRowWidth();
		}

		// if (!repeatInCurrentRow) {
		// resetGlobalRepeatTracker();
		// }

		this.rowInfos.add(currentRowInfo);
		return newRow;
	}

	// private void resetGlobalRepeatTracker() {
	// this.largestRepeatBlock = null;
	// }

	// private boolean isFirstRow() {
	// return rowInfos.size() == 0;
	// }

	protected InlineOperation handle(ApplyNextRow object) {
		// cannot handle (for now, at least)
		log.info("Cannot chart ApplyNextRow objects");
		return null;
	}

	protected InlineOperation handle(BindOffAll object) {
		log.info("Cannot chart BindOffAll objects");
		return null;
	}

	protected InlineOperation handle(DesignateEndOfRow object) {
		log.info("Cannot chart DesignateEndOfRow objects");
		return null;
	}

	protected InlineOperation handle(InlineInstruction object) {
		List<InlineOperation> newOperations = new ArrayList<InlineOperation>();
		InlineInstruction oldInlineInstruction = renderingContext
				.getPatternRepository().getInlineInstruction(object.getId());
		InlineInstruction newInlineInstruction = new InlineInstruction(
				oldInlineInstruction, newOperations);

		for (InlineOperation operation : oldInlineInstruction.getOperations()) {
			InlineOperation newOperation = handle(operation);
			// if null is returned, we cannot chart, so return null up the chain
			if (newOperation == null) {
				return null;
			}
			newOperations.add(newOperation);
		}
		return newInlineInstruction;
	}

	protected InlineOperation handle(InlineInstructionRef object) {
		InlineInstruction oldInlineInstruction = renderingContext
				.getPatternRepository().getInlineInstruction(
						object.getReferencedInstruction().getId());
		return handle(oldInlineInstruction);
	}

	protected InlineOperation handle(Repeat oldRepeat) {

		// initialize variables for this method
		KnittingEngine engine = renderingContext.getEngine();
		Until until = oldRepeat.getUntil();
		Integer value = oldRepeat.getValue();

		// ensure that we can continue to analyze this instruction for charting
		if (dynamicFirstRowCastOn && until != Until.TIMES) {
			log.info("Cannot chart this instruction because it is both defined in the header (i.e. without a knitting context) "
					+ "and the first row has a repeat section that requires a knitting context."
					+ "This can be fixed either by declaring the 'until' attribute of the repeat in the first row to be of type 'times', "
					+ "or by including the repeats in the directions "
					+ "section of the pattern instead of in the header.");
			return null;
		}
		if (currentlyRepeating) {
			log.info("Cannot currently chart nested repeats. This feature may be expanded in the future");
			return null;
		}

		// see if we need to repeat at all. If not, just return "0 repeats"
		if (!isRepeatNecessary(oldRepeat, engine)) {
			return new Repeat(Until.TIMES, 0);
		}

		// at this point, we have to do the repeat (1 or more times)
		currentlyRepeating = true;

		// start gathering up information for the new repeat. Our goal is to
		// convert all repeats
		// into an until value that represents TIMES. This is so we won't need
		// the engine to chart.
		List<DiscreteInlineOperation> newOperations = new ArrayList<DiscreteInlineOperation>();

		// gather current (pre-repeat) state
		Object engineState = engine.save();
		int originalChartWidth = this.currentRowInfo.getRowWidth();

		// walk through all the instructions of this repeat ONCE to calculate
		// the advance and increase count. If we find a work-even, remove
		// the repeat all together and completely expand the row
		boolean contextualOperationFound = false;
		for (InlineOperation operation : oldRepeat.getOperations()) {
			if (!contextualOperationFound && operation instanceof WorkEven) {
				contextualOperationFound = true;
				// if we find out there are contextual operations, clear out
				// newOperations
				// and wait for the actual repeat. We'll gather that info then.
				newOperations.clear();
				break;
			}
			InlineOperation newOperation = handle(operation);
			// if a non-discrete inline operation or null is returned, we cannot
			// chart, so return null up the chain
			if (!(newOperation instanceof DiscreteInlineOperation)) {
				return null;
			}
			newOperations.add((DiscreteInlineOperation) newOperation);
		}

		// reset the engine to the state before we repeated once
		engine.restore(engineState);
		this.currentRowInfo.setRowWidth(originalChartWidth);

		int counter = 0;
		switch (until) {
		case TIMES:
			for (int i = 0; i < value; i++) {
				// advanceAndIncrease(advanceCount, increaseCount);
				for (InlineOperation operation : oldRepeat.getOperations()) {
					DiscreteInlineOperation newOperation = (DiscreteInlineOperation) handle(operation);
					if (contextualOperationFound) {
						newOperations.add(newOperation);
					}
				}
			}
			counter = value;
			break;
		case BEFORE_END:
			while (engine.getStitchesRemainingInRow() > value) {
				// advanceAndIncrease(advanceCount, increaseCount);
				for (InlineOperation operation : oldRepeat.getOperations()) {
					DiscreteInlineOperation newOperation = (DiscreteInlineOperation) handle(operation);
					if (contextualOperationFound) {
						newOperations.add(newOperation);
					}
				}
				counter++;
			}
			break;
		case END:
			while (engine.getStitchesRemainingInRow() > 0) {
				// advanceAndIncrease(advanceCount, increaseCount);
				for (InlineOperation operation : oldRepeat.getOperations()) {
					DiscreteInlineOperation newOperation = (DiscreteInlineOperation) handle(operation);
					if (contextualOperationFound) {
						newOperations.add(newOperation);
					}
				}
				counter++;
			}
			break;
		case BEFORE_GAP:
			while (engine.getStitchesToGap() > value) {
				// advanceAndIncrease(advanceCount, increaseCount);
				for (InlineOperation operation : oldRepeat.getOperations()) {
					DiscreteInlineOperation newOperation = (DiscreteInlineOperation) handle(operation);
					if (contextualOperationFound) {
						newOperations.add(newOperation);
					}
				}
				counter++;
			}
			break;
		case BEFORE_MARKER:
			while (engine.getStitchesToNextMarker() > value) {
				// advanceAndIncrease(advanceCount, increaseCount);
				for (InlineOperation operation : oldRepeat.getOperations()) {
					DiscreteInlineOperation newOperation = (DiscreteInlineOperation) handle(operation);
					if (contextualOperationFound) {
						newOperations.add(newOperation);
					}
				}
				counter++;
			}
			break;
		case MARKER:
			while (engine.getStitchesToNextMarker() > 0) {
				// advanceAndIncrease(advanceCount, increaseCount);
				for (InlineOperation operation : oldRepeat.getOperations()) {
					DiscreteInlineOperation newOperation = (DiscreteInlineOperation) handle(operation);
					if (contextualOperationFound) {
						newOperations.add(newOperation);
					}
				}
				counter++;
			}
			break;
		default:
			throw new RuntimeException("The until value [" + until
					+ "] has not been properly coded");
		}

		currentlyRepeating = false;
		if (contextualOperationFound) {
			// make a stitch group out of all of the gathered operations if
			// we are dealing with work even operations
			return new OperationGroup(newOperations);
		} else {
			Repeat newRepeat = new Repeat();
			newRepeat.setOperations(newOperations);
			newRepeat.setUntil(Until.TIMES);
			newRepeat.setValue(counter);
			return newRepeat;
		}
	}

	protected InlineOperation handle(OperationGroup group) {
		List<DiscreteInlineOperation> newOperations = new ArrayList<DiscreteInlineOperation>();
		boolean addChildRowWidths = true;
		if (group.getSize() != null) {
			// operation group size is for cell size; if it's stated, add that value to the row width, else
			// add the advance + increase count to the row width
			addChildRowWidths = false;
			this.currentRowInfo.addToRowWidth(group.getSize());
		}
		for (DiscreteInlineOperation operation : group.getOperations()) {
			DiscreteInlineOperation newOperation = handle(operation, addChildRowWidths);
			// if null is returned, we cannot chart, so return null up the chain
			if (newOperation == null) {
				return null;
			}
			newOperations.add(newOperation);
		}
		return new OperationGroup(group.getSize(), newOperations);
	}

	/**
	 * A repeat is necessary only when its until condition has not yet been met.
	 * 
	 * @param repeat
	 * @param engine
	 * @return
	 */
	protected boolean isRepeatNecessary(Repeat repeat, KnittingEngine engine) {
		Until until = repeat.getUntil();
		Integer value = repeat.getValue();
		switch (until) {
		case TIMES:
			// repeat if we have to repeat more than 0 times
			return value > 0;
		case BEFORE_END:
			return engine.getStitchesRemainingInRow() > value;
		case END:
			return engine.getStitchesRemainingInRow() > 0;
		case BEFORE_GAP:
			return engine.getStitchesToGap() > value;
		case BEFORE_MARKER:
			return engine.getStitchesToNextMarker() > value;
		case MARKER:
			return engine.getStitchesToNextMarker() > 0;
		default:
			throw new RuntimeException("The until value [" + until
					+ "] has not been properly coded");
		}
	}

	protected InlineOperation handle(Turn object) {
		log.info("Cannot chart Turn objects");
		return null;
	}

	protected InlineOperation handle(SlipToStitchHolder object) {
		log.info("Cannot chart SlipToHolder objects");
		return null;
	}

	protected InlineOperation handle(FromStitchHolder object) {
		log.info("Cannot chart FromStitchHolder objects");
		return null;
	}

	protected InlineOperation handle(UsingNeedle object) {
		// List<InlineOperation> newOperations = new
		// ArrayList<InlineOperation>();
		// for (InlineOperation operation : object.getOperations()) {
		// InlineOperation newOperation = handle(operation);
		// if (newOperation == null) {
		// return null;
		// }
		// }
		// UsingNeedle newObject = new UsingNeedle(object.getNeedle(),
		// newOperations);
		// return newObject;

		// cannot handle (yet) after consideration. Repeats will be a little
		// tricky.
		log.info("Cannot chart UsingNeedle objects");
		return null;
	}

	protected DiscreteInlineOperation handle(IncreaseIntoNextStitch operation) {
		ChartElement element = this.chartElementFinder
				.findChartElementBy(operation.canonicalize().get(0));
		if (element == null) {
			log.info("Cannot chart an IncreaseIntoNextStitch operation because the chart element could not be identified");
			return null;
		}
		this.currentRowInfo.addToRowWidth(1);
		return handle((DiscreteInlineOperation) operation, false);

	}

	protected DiscreteInlineOperation handle(DiscreteInlineOperation operation) {
		return handle(operation, true);
	}
	
	private DiscreteInlineOperation handle(DiscreteInlineOperation operation, boolean addToRowWidth) {
		int advanceCount = operation.getAdvanceCount();
		int increaseCount = operation.getIncreaseCount();

		// can't handle colors at this point
		if (operation instanceof YarnReferenceHolder && ((YarnReferenceHolder)operation).getYarnIdRef() != null) {
			return null;
		}
		
		if (dynamicFirstRowCastOn) {
			// dynamically cast on the advance count (since all stitches are
			// unaccounted for)
			// then slip them in reverse so that we can work them "normally"
			renderingContext.getEngine().castOn(new InlineCastOn(advanceCount));
			renderingContext.getEngine().reverseSlip(advanceCount);
		}
		StitchNature stitchNature = operation instanceof StitchNatureProducer ? ((StitchNatureProducer) operation)
				.getStitchNatureProduced() : null;
		if (addToRowWidth) {
			this.currentRowInfo.addToRowWidth(advanceCount + increaseCount);
		}
		advanceAndIncrease(advanceCount, increaseCount, stitchNature);

		return operation;
	}

	protected void advanceAndIncrease(int advanceCount, int increaseCount,
			StitchNature nature) {
		if (increaseCount < 0) {
			// decrease the number specified by increaseCount
			for (int i = 0; i > increaseCount; i--) {
				renderingContext.getEngine().knitTwoTogether();
				renderingContext.getEngine().reverseSlip();
			}
		} else if (increaseCount > 0) {
			// increase the number specified by increaseCount
			for (int i = 0; i < increaseCount; i++) {
				renderingContext.getEngine().increase(new Increase(1));
				renderingContext.getEngine().reverseSlip();
			}
		}
		// advance the engine the number that this operations says to advance
		// plus the increases
		if (nature == null) {
			renderingContext.getEngine().slip(advanceCount + increaseCount);
		} else if (nature == StitchNature.PURL) {
			renderingContext.getEngine().purl(advanceCount + increaseCount);
		} else { // nature == StitchNature.KNIT
			renderingContext.getEngine().knit(advanceCount + increaseCount);
		}
	}

	protected InlineOperation handle(NoStitch object) {
		// A NoStitch indicates a blank on a chart but does not advance the
		// engine
		this.currentRowInfo
				.addToRowWidth(object.getNumberOfStitches() == null ? 1
						: object.getNumberOfStitches());
		this.containsNoStitchOperations = true;
		return object;
	}

	protected InlineOperation handle(WorkEven object) {
		// A WorkEven means to knit knits and purl purls
		int numberOfTimes = object.getNumberOfTimes() == null ? 1 : object
				.getNumberOfTimes();
		List<DiscreteInlineOperation> newOperations = new ArrayList<DiscreteInlineOperation>(
				numberOfTimes);
		for (int i = 0; i < numberOfTimes; i++) {
			StitchNature currentStitchNature = renderingContext.getEngine()
					.peekAtNextStitch().getCurrentNature();
			// reverse the stitch nature if working from the wrong side
			if (renderingContext.getEngine().getDirection() == Direction.BACKWARDS) {
				currentStitchNature = StitchNature.reverse(currentStitchNature);
			}

			// knit the knits and purl the purls
			if (currentStitchNature == StitchNature.KNIT) {
				newOperations.add(new Knit(1, null, object.getYarnIdRef()));
			} else { // lastOperation == StitchOperation.PURL
				newOperations.add(new Purl(1, null, object.getYarnIdRef()));
			}
			this.currentRowInfo.addToRowWidth(1);
			advanceAndIncrease(1, 0, currentStitchNature);
		}
		return newOperations.size() == 1 ? newOperations.get(0)
				: new OperationGroup(newOperations);
	}

	protected InlineOperation handle(PlaceMarker object) {
		renderingContext.getEngine().placeMarker();
		return object;
	}

	protected InlineOperation handle(InlineOperation operation) {
		Method m = null;
		try {
			m = this.getClass().getDeclaredMethod("handle",
					operation.getClass());
			m.setAccessible(true);
			return (InlineOperation) m.invoke(this, operation);
		} catch (NoSuchMethodException ex) {
			if (operation instanceof DiscreteInlineOperation) {
				return handle((DiscreteInlineOperation) operation);
			}
			throw new RuntimeException(
					"No handler has yet been defined for object of type "
							+ operation.getClass().getName());
		} catch (Exception ex) {
			throw new RuntimeException("An internal error occurred", ex);
		}
	}

	public int getMaxWidth() {
		return maxWidth;
	}
}
