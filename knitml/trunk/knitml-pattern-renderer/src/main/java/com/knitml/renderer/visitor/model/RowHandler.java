package com.knitml.renderer.visitor.model;

import static com.knitml.renderer.context.ContextUtils.resetLastExpressedRowNumber;
import static com.knitml.renderer.context.ContextUtils.setLastExpressedRowNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.block.Row;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class RowHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(RowHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		RenderingContext context = renderer.getRenderingContext();
		Row row = (Row) element;
		if (row.isResetRowCount()) {
			resetLastExpressedRowNumber(context);
		}

		renderer.beginRow();
		return true;
	}

	@Override
	public void end(Object element, Renderer renderer)
			throws RenderingException {
		RenderingContext context = renderer.getRenderingContext();
		renderer.endRow((Row)element,
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
