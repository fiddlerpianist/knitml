package com.knitml.validation.visitor.instruction.model;

import com.knitml.engine.common.NoMarkerFoundException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class RemoveMarkerVisitor extends AbstractPatternVisitor {
	
	public void visit(Object element, KnittingContext context) throws NoMarkerFoundException {
		context.getEngine().removeMarker();
	}


}
