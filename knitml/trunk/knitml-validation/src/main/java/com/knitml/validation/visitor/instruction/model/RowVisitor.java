package com.knitml.validation.visitor.instruction.model;

import static com.knitml.core.common.Side.RIGHT;
import static com.knitml.core.common.Side.WRONG;
import static com.knitml.engine.settings.Direction.BACKWARDS;
import static com.knitml.engine.settings.Direction.FORWARDS;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.EnumUtils;
import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.Side;
import com.knitml.core.model.directions.block.Row;
import com.knitml.engine.KnittingEngine;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.common.NotEndOfRowException;
import com.knitml.engine.common.UnexpectedRowNumberException;
import com.knitml.engine.common.UnexpectedSideException;
import com.knitml.engine.common.WrongKnittingShapeException;
import com.knitml.engine.settings.Direction;
import com.knitml.validation.common.InvalidStructureException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class RowVisitor extends AbstractPatternVisitor {

	private final static Logger log = LoggerFactory.getLogger(RowVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		Row row = (Row) element;
		KnittingShape currentShape = context.getEngine().getKnittingShape();
		// check for knitting shape and the validity of what is being requested
		if (row.getType() != null && row.getType() != currentShape) {
			throw new WrongKnittingShapeException(MessageFormat.format(
					Messages.getString("RowVisitor.WRONG_SHAPE"), //$NON-NLS-1$
					EnumUtils.fromEnum(row.getType()),
					EnumUtils.fromEnum(currentShape)));
		}

		// tell engine what to do with the current row
		if (row.isResetRowCount()) {
			log.debug("Resetting the row number and clearing instructions"); //$NON-NLS-1$
			context.getEngine().resetRowNumber();
			context.getPatternRepository().clearLocalInstructions();
		}
		boolean isNotShortRow = !row.isShortRow();
		// start a new row if it's a complete row (i.e. not a short row)
		// and the work is currently between rows
		if (isNotShortRow || context.getEngine().isBetweenRows()) {
			if (row.isLongRow()) {
				context.getEngine().startNewLongRow();
			} else {
				context.getEngine().startNewRow();
			}
		} else {
			// artificially increment the row number, even though the row is not
			// at the beginning
			context.getEngine().incrementCurrentRowNumber();
		}

		if (context.getEngine().getTotalNumberOfStitchesInRow() == 0) {
			// We are starting with no stitches on the needles.
			// When this is the case, we need to be told which side to start on
			if (row.getSide() == null) {
				throw new InvalidStructureException(
						Messages.getString("RowVisitor.ROW_SIDE_REQUIRED_WHEN_NO_STITCHES")); //$NON-NLS-1$
			}
			Side side = row.getSide();
			KnittingEngine engine = context.getEngine();
			if ((side == RIGHT && engine.getDirection() == BACKWARDS)
					|| (side == WRONG && engine.getDirection() == FORWARDS)) {
				context.getEngine().turn();
			}
		}

		// if the row isn't in the middle of a replay and the pattern asks the
		// validator to fill in the side of work, provide that information here
		if (!(context.getPatternState().isReplayMode())) {
			if (row.isInformSide()) {
				Direction direction = context.getEngine().getDirection();
				if (direction == FORWARDS) {
					row.setSide(RIGHT);
				} else {
					row.setSide(WRONG);
				}
				row.setInformSide(false);
			} else if (row.getSide() != null) {
				// validate that the side provided is the side that we are
				// currently knitting
				Side expectedSide = row.getSide();
				Direction direction = context.getEngine().getDirection();
				Side actualSide = direction == Direction.FORWARDS ? RIGHT
						: WRONG;
				if (actualSide != expectedSide) {
					throw new UnexpectedSideException(expectedSide, actualSide);
				}
			}

		}

		manageRowNumbers(row, context);
		int localRowNumberFromEngine = context.getEngine()
				.getCurrentRowNumber();
		context.getPatternState().setAsCurrent(row, localRowNumberFromEngine);

		if (!context.getPatternState().isReplayMode()
				&& row.getNumbers() != null && row.getNumbers().length > 0) {
			// if a row number is specified, validate it against the expected
			// row number
			validateCurrentRowNumber(row, localRowNumberFromEngine, context);
		}
		visitChildren(row, context);
		// clears any "fixed" row information which has been applied in this row
		context.getPatternState().clearActiveRowsForInstructions();

		if (row.getFollowupInformation() != null) {
			visitChild(row.getFollowupInformation(), context);
		}

		// if we specify that this row is a complete row, OR if we get to the
		// end of the row when we're finished, call endRow()
		if (isNotShortRow || context.getEngine().isEndOfRow()) {
			context.getEngine().endRow();
		}
		log.debug(
				"Row {} (local row {}) completed: {} stitches", new Object[] { context.getEngine().getTotalRowsCompleted(), localRowNumberFromEngine, context.getEngine() //$NON-NLS-1$
								.getTotalNumberOfStitchesInRow() });

		context.getPatternState().clearCurrentRow();
	}

	private void manageRowNumbers(Row row, KnittingContext context)
			throws InvalidStructureException, UnexpectedRowNumberException,
			NotEndOfRowException {
		// if no row number is currently specified AND the assign-row-number
		// attribute isn't set to false,
		// assign the 'number' attribute to be the current row number
		if (!(context.getPatternState().isReplayMode())
				&& (row.getNumbers() == null || row.getNumbers().length == 0)
				&& (row.getAssignRowNumber() == null || row
						.getAssignRowNumber() == true)) {
			row.setNumber(context.getEngine().getCurrentRowNumber());
			row.setAssignRowNumber(null);
		}
	}

	/**
	 * 
	 * @param row
	 *            a Row with at least one row number specified
	 * @param context
	 *            the knitting context
	 * @throws UnexpectedRowNumberException
	 */
	private void validateCurrentRowNumber(Row row, int engineRowNumber,
			KnittingContext context) throws UnexpectedRowNumberException {
		//
		int[] numbers = row.getNumbers();
		if (numbers.length > 1
				&& !(context.getPatternState().isWithinInstruction())) {
			throw new InvalidStructureException(
					Messages.getString("RowVisitor.MULTIPLE_ROWS_NOT_ALLOWED_OUTSIDE_INSTRUCTION")); //$NON-NLS-1$
		} else if (numbers[0] != engineRowNumber
				&& (!context.getPatternState().isWithinInstruction() || context
						.getPatternState().isAtFirstRowWithinInstruction())) {
			// you can manually reset the row count by setting a row's
			// number to 1
			if (numbers[0] != 1) {
				throw new UnexpectedRowNumberException(engineRowNumber,
						numbers[0]);
			}
			context.getEngine().resetRowNumber();
			context.getEngine().incrementCurrentRowNumber();
		}
	}

}
