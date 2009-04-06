package com.knitml.validation.visitor.instruction.model;

import com.knitml.core.model.directions.inline.Slip;
import com.knitml.core.common.SlipDirection;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class SlipVisitor extends AbstractPatternVisitor {

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		Slip spec = (Slip) element;
		if (spec.getDirection() == SlipDirection.REVERSE) {
			context.getEngine().reverseSlip(spec.getNumberOfTimes() == null ? 1 : spec.getNumberOfTimes());
		} else {
			context.getEngine().slip(spec);
		}
	}

}
