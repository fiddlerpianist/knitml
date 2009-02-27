package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.UsingNeedle;
import com.knitml.core.model.header.Needle;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class UsingNeedleVisitor extends AbstractRenderingEvent {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(UsingNeedleVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		UsingNeedle operation = (UsingNeedle) element;
		Needle needle = operation.getNeedle();
		if (needle == null) {
			throw new RenderingException("Cannot find needle for using-needle operation in the pattern repository");
		}
		context.getRenderer().beginUsingNeedle(needle);
		return true;
	}
	
	public void end(Object element, RenderingContext context) {
		context.getRenderer().endUsingNeedle();
	}

}
