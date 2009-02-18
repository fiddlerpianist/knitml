package com.knitml.validation.visitor.instruction.model;

import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class NoStitchVisitor extends AbstractValidationVisitor {

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		// A NoStitch simply provides a hint to a renderer, so there is nothing to validate
		return;
	}

}
