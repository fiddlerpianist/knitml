package com.knitml.validation.visitor.definition.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Row;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class RowVisitor extends AbstractPatternVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(RowVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		Row row = (Row) element;
		// if no row number is currently specified AND the assign-row-number
		// attribute isn't set to false,
		// assign the 'number' attribute to be the current row number
		if (row.getNumbers() == null) {
			row.setNumbers(new int[] { context.getPatternState()
					.getHeaderRowNumber() });
			context.getPatternState().setHeaderRowNumber(
					context.getPatternState().getHeaderRowNumber() + 1);
		}
	}

}
