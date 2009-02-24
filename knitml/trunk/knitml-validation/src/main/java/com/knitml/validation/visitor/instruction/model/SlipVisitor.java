package com.knitml.validation.visitor.instruction.model;

import com.knitml.core.model.directions.inline.Slip;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class SlipVisitor extends AbstractPatternVisitor {

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		context.getEngine().slip((Slip)element);
	}

}
