package com.knitml.renderer.visitor.model;

import static com.knitml.renderer.context.ContextUtils.resetLastExpressedRowNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class DirectionsVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(DirectionsVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		resetLastExpressedRowNumber(context);
		context.getRenderer().beginDirections();
		return true;
	}

	public void end(Object element, RenderingContext context)
	throws RenderingException {
		context.getRenderer().endDirections();
	}
}
