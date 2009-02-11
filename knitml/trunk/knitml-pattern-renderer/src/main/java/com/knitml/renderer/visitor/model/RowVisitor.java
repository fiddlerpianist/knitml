package com.knitml.renderer.visitor.model;

import static com.knitml.renderer.context.ContextUtils.resetLastExpressedRowNumber;
import static com.knitml.renderer.context.ContextUtils.setLastExpressedRowNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Row;
import com.knitml.engine.KnittingEngine;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class RowVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(RowVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		Row row = (Row) element;
		if (row.getType() != null) {
			// set the current shape to be whatever the row says it is (if it
			// says anything at all)
			context.getPatternState().setCurrentKnittingShape(row.getType());
		}
		if (row.isResetRowCount()) {
			resetLastExpressedRowNumber(context);
		}

		engineBeginRow(row, context.getEngine());
		context.getRenderer().beginRow(row,
				context.getPatternState().getCurrentKnittingShape());
		visitChildren(row, context);
		if (row.getFollowupInformation() != null) {
			visitChild(row.getFollowupInformation(), context);
		}
		context.getRenderer().endRow();
		engineEndRow(row, context.getEngine());

		int[] rowNumbers = row.getNumbers();
		// record the last row number that was rendered (only if not within an
		// instruction)
		if (!(context.getPatternState().hasCurrentInstruction())) {
			if (rowNumbers == null) {
				// if the row number is not explicitly specified, infer it by
				// adding 1 to the previously recorded one
				setLastExpressedRowNumber(context.getPatternState()
						.getLastExpressedRowNumber() + 1, context);
			} else {
				// if this row is not executing within instruction, it only has
				// one number
				setLastExpressedRowNumber(row.getNumbers()[0], context);
			}
		}

	}

	private void engineBeginRow(Row row, KnittingEngine engine) {
		// tell engine what to do with the current row
		if (row.isResetRowCount()) {
			engine.resetRowNumber();
		}
		boolean isCompleteRow = !row.isShortRow();
		// start a new row if it's a complete row (i.e. not a short row)
		// and the work is currently between rows
		if (isCompleteRow || engine.isBetweenRows()) {
			engine.startNewRow();
		} else {
			// artificially increment the row number, even though the row is not
			// at the beginning
			engine.incrementCurrentRowNumber();
		}
	}
	
	private void engineEndRow(Row row, KnittingEngine engine) {
		// visitChildren here

		// if we specify that this row is a complete row, OR if we get to the
		// end of the row when we're finished, call endRow()
		if (!row.isShortRow() || engine.isEndOfRow()) {
			engine.endRow();
		}
	}	
}
