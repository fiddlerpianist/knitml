package com.knitml.validation.visitor.instruction.impl;

import com.knitml.core.model.operations.CompositeOperation;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.common.NoGapFoundException;
import com.knitml.engine.common.NoMarkerFoundException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.NameResolver;
import com.knitml.validation.visitor.instruction.Visitor;
import com.knitml.validation.visitor.instruction.VisitorFactory;

public abstract class AbstractPatternVisitor implements Visitor {

	private VisitorFactory visitorFactory;

	protected void visitChildren(CompositeOperation operation,
			KnittingContext context) throws KnittingEngineException {
		for (Object child : operation.getOperations()) {
			visitChild(child, context);
		}
	}

	protected void visitChild(Object child, KnittingContext context)
			throws KnittingEngineException {
		if (child != null) {
			context.getListenerManager().fireBegin(child, context);
			Visitor visitor = getVisitorFactory().findVisitorFromClassName(
					child);
			visitor.visit(child, context);
			context.getListenerManager().fireEnd(child, context);
		}
	}

	protected int getStitchesBeforeEnd(KnittingContext context) {
		if (context.isNeedleInstructions()) {
			return context.getEngine().getStitchesRemainingOnCurrentNeedle();
		} else {
			return context.getEngine().getStitchesRemainingInRow();
		}
	}

	protected int getStitchesBeforeMarker(KnittingContext context)
			throws NoMarkerFoundException {
		return context.getEngine().getStitchesToNextMarker();
	}

	protected int getStitchesBeforeGap(KnittingContext context)
			throws NoGapFoundException {
		return context.getEngine().getStitchesToGap();
	}

	private VisitorFactory getVisitorFactory() {
		return visitorFactory;
	}

	public void setVisitorFactory(VisitorFactory visitorFactory) {
		this.visitorFactory = visitorFactory;
	}

	protected void pushNameResolver(NameResolver resolver) {
		getVisitorFactory().pushNameResolver(resolver);
	}

	protected void popNameResolver() {
		getVisitorFactory().popNameResolver();
	}

}
