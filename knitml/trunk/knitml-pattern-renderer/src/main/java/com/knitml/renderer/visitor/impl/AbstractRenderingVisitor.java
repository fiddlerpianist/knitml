package com.knitml.renderer.visitor.impl;

import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.RenderingVisitor;
import com.knitml.renderer.visitor.VisitorFactory;

public abstract class AbstractRenderingVisitor implements RenderingVisitor {

	private VisitorFactory visitorFactory;

	// protected void visitChildren(CompositeOperation operation,
	// RenderingContext context) throws RenderingException {
	// PatternState state = context.getPatternState();
	// try {
	// state.getOperationTree().push(operation);
	// for (Operation childOperation : operation.getOperations()) {
	// RenderingVisitor visitor = getVisitorFactory()
	// .findVisitorFromClassName(childOperation);
	// visitor.visit(childOperation, context);
	// }
	// } finally {
	// state.getOperationTree().pop();
	// }
	// }
	//
//	protected void visitChildOld(Object childObject, RenderingContext context)
//			throws RenderingException {
//		if (childObject != null) {
//			RenderingVisitor visitor = getVisitorFactory()
//					.findVisitorFromClassName(childObject);
//			visitor.visit(childObject, context);
//		}
//	}
//
//	protected void visitSubstitute(Object object, RenderingContext context)
//			throws RenderingException {
//		if (object != null) {
//			RenderingVisitor visitor = getVisitorFactory()
//					.findVisitorFromClassName(object);
//			visitor.visit(object, context);
//		}
//	}
//
	public VisitorFactory getVisitorFactory() {
		return visitorFactory;
	}

	public void setVisitorFactory(VisitorFactory visitorFactory) {
		this.visitorFactory = visitorFactory;
	}

	// temporary methods for migration purposes
	public void end(Object object, RenderingContext context)
			throws RenderingException {
	}

}
