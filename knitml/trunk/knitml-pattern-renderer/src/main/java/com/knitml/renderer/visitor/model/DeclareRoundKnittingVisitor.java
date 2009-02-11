package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.KnittingShape;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class DeclareRoundKnittingVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(DeclareRoundKnittingVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		context.getPatternState().setCurrentKnittingShape(KnittingShape.ROUND);
		context.getEngine().declareRoundKnitting();
		context.getRenderer().renderDeclareRoundKnitting();
	}

}
