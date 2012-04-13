package com.knitml.validation.visitor.instruction.model;

import com.knitml.core.model.operations.inline.BindOffAll;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class BindOffAllVisitor extends AbstractPatternVisitor {

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		context.getEngine().bindOffAll((BindOffAll) element);
	}

}
