package com.knitml.validation.visitor.instruction.impl;

import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.common.NoGapFoundException;
import com.knitml.engine.common.NoMarkerFoundException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.Visitor;
import com.knitml.validation.visitor.instruction.VisitorFactory;

public abstract class AbstractValidationVisitor implements Visitor {
	
	private VisitorFactory visitorFactory;

	protected void visitChildren(CompositeOperation operation, KnittingContext context) throws KnittingEngineException {
		for (Object child : operation.getOperations()) {
			Visitor visitor = getVisitorFactory().findVisitorFromClassName(child);
			visitor.visit(child, context);
		}
	}

	protected void visitChild(Object childObject, KnittingContext context) throws KnittingEngineException {
		if (childObject != null) {
			Visitor visitor = getVisitorFactory().findVisitorFromClassName(
					childObject);
			visitor.visit(childObject, context);
		}
	}

	protected int getStitchesBeforeEnd(KnittingContext context) {
		if (context.isNeedleInstructions()) {
			return context.getEngine().getStitchesRemainingOnCurrentNeedle();
		} else {
			return context.getEngine().getStitchesRemainingInRow();
		}
	}

	protected int getStitchesBeforeMarker(KnittingContext context) throws NoMarkerFoundException {
		return context.getEngine().getStitchesToNextMarker();
	}

	protected int getStitchesBeforeGap(KnittingContext context) throws NoGapFoundException {
		return context.getEngine().getStitchesToGap();
	}
	
	public VisitorFactory getVisitorFactory() {
		return visitorFactory;
	}

	public void setVisitorFactory(VisitorFactory visitorFactory) {
		this.visitorFactory = visitorFactory;
	}

}
