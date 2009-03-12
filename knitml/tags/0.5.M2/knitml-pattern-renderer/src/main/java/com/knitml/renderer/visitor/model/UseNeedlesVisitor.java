package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.UseNeedles;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class UseNeedlesVisitor extends AbstractRenderingEvent {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(UseNeedlesVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		UseNeedles useNeedles = (UseNeedles) element;
		if (!useNeedles.isSilentRendering()) {
			context.getRenderer().renderUseNeedles(useNeedles.getNeedles());
		}
		return true;
	}

}
