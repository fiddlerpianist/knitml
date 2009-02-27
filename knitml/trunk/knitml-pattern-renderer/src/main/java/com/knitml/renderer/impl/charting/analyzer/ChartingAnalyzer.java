package com.knitml.renderer.impl.charting.analyzer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.model.directions.DiscreteInlineOperation;
import com.knitml.core.model.directions.InlineOperation;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.core.model.directions.inline.ApplyNextRow;
import com.knitml.core.model.directions.inline.BindOffAll;
import com.knitml.core.model.directions.inline.DesignateEndOfRow;
import com.knitml.core.model.directions.inline.Increase;
import com.knitml.core.model.directions.inline.InlineCastOn;
import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.core.model.directions.inline.InlineInstructionRef;
import com.knitml.core.model.directions.inline.NoStitch;
import com.knitml.core.model.directions.inline.PlaceMarker;
import com.knitml.core.model.directions.inline.Repeat;
import com.knitml.core.model.directions.inline.Turn;
import com.knitml.core.model.directions.inline.UsingNeedle;
import com.knitml.core.model.directions.inline.Repeat.Until;
import com.knitml.engine.KnittingEngine;
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

	private RenderingContext renderingContext;
	/**
	 * Set this variable to true to indicate that the KnittingEngine is not
	 * immediately in a state where knitting can commence, i.e. a pattern is
	 * processing an instruction definition in the header rather than in the
	 * formal directions section of the pattern.
	 */
	private boolean dynamicFirstRowCastOn = false;
	private int maxWidth = 0;
	private int currentChartWidth = 0;
	private boolean currentlyRepeating = false;
	private KnittingShape shape = KnittingShape.FLAT;
	private final static Logger log = LoggerFactory
			.getLogger(ChartingAnalyzer.class);

	public ChartingAnalyzer(RenderingContext context) {
		this.renderingContext = context;
	}

	public Analysis analyzeInstruction(Instruction originalInstruction,
			boolean fromInstructionDefinition) {
		this.dynamicFirstRowCastOn = fromInstructionDefinition;
		this.maxWidth = 0;
		this.currentChartWidth = 0;
		this.currentlyRepeating = false;
		this.shape = KnittingShape.FLAT;

		Object engineState = renderingContext.getEngine().save();

		try {
			List<Row> newRows = new ArrayList<Row>();
			Instruction newInstruction = new Instruction(originalInstruction,
					newRows);
			if (!originalInstruction.hasRows()) {
				// cannot handle for-each-row-in-instruction yet. Possibly this
				// would be
				// expanded ahead of time.
				log
						.info("Cannot handle an Instruction whose operations are not made up entirely of Rows");
				return new Analysis();
			}

			for (Row originalRow : originalInstruction.getRows()) {
				Row newRow = analyzeRow(originalRow);
				// if null is returned, we cannot chart, so return null up the
				// chain
				if (newRow == null) {
					return new Analysis();
				}
				newRows.add(newRow);
			}
			Analysis result = new Analysis();
			result.setInstructionToUse(newInstruction);
			result.setChartable(true);
			result.setMaxWidth(this.maxWidth);
			result.setKnittingShape(this.shape);
			return result;
		} finally {
			renderingContext.getEngine().restore(engineState);
		}
	}

	protected Row analyzeRow(Row originalRow) {
		KnittingEngine engine = renderingContext.getEngine();
		// TODO return modified row, if applicable
		List<InlineOperation> newOperations = new ArrayList<InlineOperation>();
		Row newRow = new Row(originalRow, newOperations);
		this.currentChartWidth = 0;

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
			engine.knitTwoTogether();
			// now pretend like all is normal
			dynamicFirstRowCastOn = false;
		}
		engine.endRow();

		// if this row pushed the bounds of the previously recorded maximum
		// width, record the new max width
		if (currentChartWidth > maxWidth) {
			maxWidth = currentChartWidth;
		}

		return newRow;
	}

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

		Until until = oldRepeat.getUntil();
		Integer value = oldRepeat.getValue();

		if (dynamicFirstRowCastOn && until != Until.TIMES) {
			log
					.info("Cannot chart this instruction because it is both defined in the header (i.e. without a knitting context) "
							+ "and the first row has a repeat section that requires a knitting context."
							+ "This can be fixed either by declaring the 'until' attribute of the repeat in the first row to be of type 'times', "
							+ "or by including the repeats in the directions "
							+ "section of the pattern instead of in the header.");
			return null;
		}
		if (currentlyRepeating) {
			log
					.info("Cannot currently chart nested repeats. This feature may be expanded in the future");
			return null;
		}

		currentlyRepeating = true;

		List<InlineOperation> newOperations = new ArrayList<InlineOperation>();
		Repeat newRepeat = new Repeat();
		newRepeat.setOperations(newOperations);

		KnittingEngine engine = renderingContext.getEngine();
		int startingTotalStitches = engine
				.getTotalNumberOfStitchesOnCurrentNeedle();
		int startingStitchesRemaining = engine
				.getStitchesRemainingOnCurrentNeedle();
		Object engineState = engine.save();
		int savedCurrentWidth = this.currentChartWidth;
		// TODO handle the case where we should have 0 repeats
		for (InlineOperation operation : oldRepeat.getOperations()) {
			InlineOperation newOperation = handle(operation);
			// if null is returned, we cannot chart, so return null up the chain
			if (newOperation == null) {
				return null;
			}
			newOperations.add(newOperation);
		}
		int endingTotalStitches = engine
				.getTotalNumberOfStitchesOnCurrentNeedle();
		int endingStitchesRemaining = engine
				.getStitchesRemainingOnCurrentNeedle();
		engine.restore(engineState);
		this.currentChartWidth = savedCurrentWidth;

		int advanceCount = startingStitchesRemaining - endingStitchesRemaining;
		int increaseCount = endingTotalStitches - startingTotalStitches;

		int counter = 0;
		switch (until) {
		case TIMES:
			for (int i = 0; i < value; i++) {
				advanceAndIncrease(advanceCount, increaseCount);
			}
			counter = value;
			break;
		case BEFORE_END:
			while (renderingContext.getEngine().getStitchesRemainingInRow() > value) {
				advanceAndIncrease(advanceCount, increaseCount);
				counter++;
			}
			break;
		case END:
			while (renderingContext.getEngine().getStitchesRemainingInRow() > 0) {
				advanceAndIncrease(advanceCount, increaseCount);
				counter++;
			}
			break;
		case BEFORE_GAP:
			while (renderingContext.getEngine().getStitchesToGap() > value) {
				advanceAndIncrease(advanceCount, increaseCount);
				counter++;
			}
			break;
		case BEFORE_MARKER:
			while (renderingContext.getEngine().getStitchesToNextMarker() > value) {
				advanceAndIncrease(advanceCount, increaseCount);
				counter++;
			}
			break;
		case MARKER:
			while (renderingContext.getEngine().getStitchesToNextMarker() > 0) {
				advanceAndIncrease(advanceCount, increaseCount);
				counter++;
			}
			break;
		default:
			throw new RuntimeException("The until value [" + until
					+ "] has not been properly coded");
		}

		newRepeat.setUntil(Until.TIMES);
		newRepeat.setValue(counter);
		currentlyRepeating = false;
		return newRepeat;
	}

	protected InlineOperation handle(Turn object) {
		log.info("Cannot chart Turn objects");
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

	protected InlineOperation handle(DiscreteInlineOperation object) {
		int advanceCount = object.getAdvanceCount();
		int increaseCount = object.getIncreaseCount();

		if (dynamicFirstRowCastOn) {
			// dynamically cast on the advance count (since all stitches are
			// unaccounted for)
			// then slip them in reverse so that we can work them "normally"
			renderingContext.getEngine().castOn(new InlineCastOn(advanceCount));
			renderingContext.getEngine().reverseSlip(advanceCount);
		}
		advanceAndIncrease(advanceCount, increaseCount);

		return object;
	}

	protected void advanceAndIncrease(int advanceCount, int increaseCount) {
		this.currentChartWidth += advanceCount + increaseCount;
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
		renderingContext.getEngine().slip(advanceCount + increaseCount);
	}

	protected InlineOperation handle(NoStitch object) {
		// A NoStitch indicates a blank on a chart but does not advance the
		// engine
		this.currentChartWidth += object.getNumberOfStitches() == null ? 1
				: object.getNumberOfStitches();
		return object;
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
