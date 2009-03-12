package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.Increase;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class IncreaseVisitor extends AbstractRenderingEvent {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(IncreaseVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		Increase increase = (Increase) element;
		context.getRenderer().renderIncrease(increase);
		return true;
	}
}
