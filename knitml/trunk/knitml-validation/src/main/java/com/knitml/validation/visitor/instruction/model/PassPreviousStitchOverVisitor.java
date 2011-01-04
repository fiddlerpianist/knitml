package com.knitml.validation.visitor.instruction.model;

import com.knitml.core.model.directions.inline.PassPreviousStitchOver;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class PassPreviousStitchOverVisitor extends AbstractPatternVisitor {

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		PassPreviousStitchOver spec = (PassPreviousStitchOver) element;
		int numberOfStitches = spec.getNumberOfTimes() == null ? 1 : spec.getNumberOfTimes();
		for (int i=0; i < numberOfStitches; i++) {
			context.getEngine().passPreviousStitchOver();
		}
	}

}
