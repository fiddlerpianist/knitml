package com.knitml.renderer.visitor.impl;

import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.directions.Operation;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.PatternState;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.RenderingVisitor;
import com.knitml.renderer.visitor.VisitorFactory;

public abstract class AbstractRenderingVisitor implements RenderingVisitor {

	private VisitorFactory visitorFactory;

	protected void visitChildren(CompositeOperation operation,
			RenderingContext context) throws RenderingException {
		PatternState state = context.getPatternState();
		try {
			state.getOperationTree().push(operation);
			for (Operation childOperation : operation.getOperations()) {
				RenderingVisitor visitor = getVisitorFactory()
						.findVisitorFromClassName(childOperation);
				visitor.visit(childOperation, context);
			}
		} finally {
			state.getOperationTree().pop();
		}
	}

	protected void visitChild(Object childObject, RenderingContext context)
			throws RenderingException {
		if (childObject != null) {
			RenderingVisitor visitor = getVisitorFactory()
					.findVisitorFromClassName(childObject);
			visitor.visit(childObject, context);
		}
	}

	public VisitorFactory getVisitorFactory() {
		return visitorFactory;
	}

	public void setVisitorFactory(VisitorFactory visitorFactory) {
		this.visitorFactory = visitorFactory;
	}

}
