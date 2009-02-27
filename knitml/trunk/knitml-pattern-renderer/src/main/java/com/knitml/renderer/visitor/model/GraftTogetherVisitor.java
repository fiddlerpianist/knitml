package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.GraftTogether;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class GraftTogetherVisitor extends AbstractRenderingEvent {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(GraftTogetherVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		GraftTogether operation = (GraftTogether)element;
		context.getRenderer().renderGraftStitchesTogether(operation.getNeedles());
		return true;
	}

}
