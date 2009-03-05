package com.knitml.renderer.visitor.model;

import static com.knitml.renderer.context.ContextUtils.resetLastExpressedRowNumber;
import static com.knitml.renderer.context.ContextUtils.setLastExpressedRowNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Row;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class RowVisitor extends AbstractRenderingEvent {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(RowVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		Row row = (Row) element;
		if (row.isResetRowCount()) {
			resetLastExpressedRowNumber(context);
		}

		context.getRenderer().beginRow();
		return true;
	}

	public void end(Object element, RenderingContext context)
			throws RenderingException {
		context.getRenderer().endRow((Row)element,
				context.getEngine().getKnittingShape());

		Row row = (Row) element;
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
}
