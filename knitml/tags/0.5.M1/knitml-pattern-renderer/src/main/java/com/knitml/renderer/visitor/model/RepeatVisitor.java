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

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		Repeat repeat = (Repeat) element;
		context.getRenderer().beginRepeat(repeat);
		return true;
	}

	public void end(Object element, RenderingContext context) {
		Repeat repeat = (Repeat) element;
		context.getRenderer().endRepeat(repeat.getUntil(), repeat.getValue());
	}
}
