package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.KnittingShape;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class JoinInRoundVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(JoinInRoundVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		context.getPatternState().setCurrentKnittingShape(KnittingShape.ROUND);
		context.getRenderer().renderJoinInRound();
	}

}
