package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.Knit;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class KnitVisitor extends AbstractRenderingEvent {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(KnitVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		Knit knit = (Knit)element;
		context.getRenderer().renderKnit(knit);
		return true;
	}
}
