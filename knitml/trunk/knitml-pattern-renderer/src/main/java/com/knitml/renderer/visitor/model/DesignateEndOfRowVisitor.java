package com.knitml.renderer.visitor.model;

import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class DesignateEndOfRowVisitor extends AbstractRenderingEvent {

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		context.getRenderer().renderDesignateEndOfRow(context.getEngine().getKnittingShape());
		return true;
	}
}
