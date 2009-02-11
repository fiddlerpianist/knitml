package com.knitml.validation.visitor.instruction.model;

import com.knitml.core.model.directions.inline.CrossStitches;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class CrossStitchesVisitor extends AbstractValidationVisitor {

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		context.getEngine().crossStitches((CrossStitches)element);
	}

}
