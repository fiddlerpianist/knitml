package com.knitml.validation.visitor.instruction;

import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;

public interface Visitor {
	void visit(Object operation, KnittingContext context) throws KnittingEngineException;
}
