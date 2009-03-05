package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.DeclareFlatKnitting;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class DeclareFlatKnittingVisitor extends AbstractRenderingEvent {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(DeclareFlatKnittingVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		DeclareFlatKnitting spec = (DeclareFlatKnitting) element;
		context.getRenderer().renderDeclareFlatKnitting((DeclareFlatKnitting)element);
		return true;
	}

}
