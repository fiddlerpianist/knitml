package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.EnumUtils;
import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.Side;
import com.knitml.core.model.directions.block.Row;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.common.NotEndOfRowException;
import com.knitml.engine.common.UnexpectedRowNumberException;
import com.knitml.engine.common.WrongKnittingShapeException;
import com.knitml.engine.settings.Direction;
import com.knitml.validation.common.InvalidStructureException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class RowVisitor extends AbstractValidationVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(RowVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		Row row = (Row) element;
		KnittingShape currentShape = context.getEngine().getKnittingShape();
		// check for knitting shape and the validity of what is being requested
		if (row.getType() != null && row.getType() != currentShape) {
			throw new WrongKnittingShapeException(
					"The shape specified in this row ("
							+ EnumUtils.fromEnum(row.getType())
							+ ") does not match the expected shape ("
							+ EnumUtils.fromEnum(currentShape) + ")");
		}

		// tell engine what to do with the current row
		if (row.isResetRowCount()) {
			context.getEngine().resetRowNumber();
			context.getPatternRepository().clearLocalInstructions();
		}
		boolean isCompleteRow = !row.isShortRow();
		// start a new row if it's a complete row (i.e. not a short row)
		// and the work is currently between rows
		if (isCompleteRow || context.getEngine().isBetweenRows()) {
			context.getEngine().startNewRow();
		} else {
			// artificially increment the row number, even though the row is not
			// at the beginning
			context.getEngine().incrementCurrentRowNumber();
		}

		// if the row isn't in the middle of a replay and the pattern asks the
		// validator to fill in the side of work, provide that information here
		if (!(context.getPatternState().isReplayMode())) {
			if (row.isInformSide()) {
				Direction direction = context.getEngine().getDirection();
				if (direction == Direction.FORWARDS) {
					row.setSide(Side.RIGHT);
				} else {
					row.setSide(Side.WRONG);
				}
				row.setInformSide(false);
			} else if (row.getSide() != null) {
				// validate that the side provided is the side that we are
				// currently knitting
				Side expectedSide = row.getSide();
				Direction direction = context.getEngine().getDirection();
				Side actualSide = direction == Direction.FORWARDS ? Side.RIGHT
						: Side.WRONG;
				if (actualSide != expectedSide) {
					throw new KnittingEngineException("Expected "
							+ expectedSide + " side of knitting but was "
							+ actualSide);
				}
			}

		}

		manageRowNumbers(row, context);
		visitChildren(row, context);
		if (row.getFollowupInformation() != null) {
			visitChild(row.getFollowupInformation(), context);
		}

		// if we specify that this row is a complete row, OR if we get to the
		// end of the row when we're finished, call endRow()
		if (isCompleteRow || context.getEngine().isEndOfRow()) {
			context.getEngine().endRow();
		}
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
			row.setNumbers(new int[] { context.getEngine()
					.getCurrentRowNumber() });
			row.setAssignRowNumber(null);
		} else if (row.getNumbers() != null
				&& !(context.getPatternState().isWithinInstruction())) {
			// if a number is specified and the row is not in the confines of an
			// instruction, validate it against the expected row number
			int[] numbers = row.getNumbers();
			if (numbers.length > 1) {
				throw new InvalidStructureException(
						"Multiple rows per element cannot be specified outside of an instruction element");
			} else if (numbers[0] != context.getEngine().getCurrentRowNumber()) {
				// you can manually reset the row count by setting a row's
				// number to 1
				if (numbers[0] != 1) {
					throw new UnexpectedRowNumberException(context.getEngine()
							.getCurrentRowNumber(), numbers[0]);
				}
				context.getEngine().resetRowNumber();
				context.getEngine().incrementCurrentRowNumber();
			}
		}
	}

}
