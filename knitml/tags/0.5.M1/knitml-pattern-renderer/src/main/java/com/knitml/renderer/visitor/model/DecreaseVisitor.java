package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.Decrease;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class DecreaseVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(DecreaseVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		Decrease decrease = (Decrease) element;
		context.getRenderer().renderDecrease(decrease);
		return true;
	}
	
}
