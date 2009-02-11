package com.knitml.renderer.visitor.model;

import static com.knitml.renderer.context.ContextUtils.resetLastExpressedRowNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.Directions;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class DirectionsVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(DirectionsVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		Directions directions = (Directions)element;
		resetLastExpressedRowNumber(context);
		context.getRenderer().beginDirections();
		visitChildren(directions, context);
		context.getRenderer().endDirections();
	}

}
