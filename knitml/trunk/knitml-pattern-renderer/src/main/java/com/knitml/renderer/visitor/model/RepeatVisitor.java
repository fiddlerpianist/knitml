package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.Repeat;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class RepeatVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(RepeatVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		Repeat repeat = (Repeat) element;
		context.getRenderer().beginRepeat(repeat);
		visitChildren(repeat, context);
		context.getRenderer().endRepeat(repeat.getUntil(), repeat.getValue());
	}

}
