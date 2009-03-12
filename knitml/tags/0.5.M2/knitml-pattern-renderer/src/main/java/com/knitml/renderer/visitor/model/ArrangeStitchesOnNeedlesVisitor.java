package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.ArrangeStitchesOnNeedles;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class ArrangeStitchesOnNeedlesVisitor extends AbstractRenderingEvent {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(ArrangeStitchesOnNeedlesVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		ArrangeStitchesOnNeedles operation = (ArrangeStitchesOnNeedles) element;
		context.getRenderer().renderArrangeStitchesOnNeedles(
				operation.getNeedles());
		return true;
	}

}
