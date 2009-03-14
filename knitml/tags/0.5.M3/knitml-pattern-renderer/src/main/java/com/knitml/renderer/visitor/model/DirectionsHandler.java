package com.knitml.renderer.visitor.model;

import static com.knitml.renderer.context.ContextUtils.resetLastExpressedRowNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class DirectionsHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(DirectionsHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		resetLastExpressedRowNumber(renderer.getRenderingContext());
		renderer.beginDirections();
		return true;
	}

	public void end(Object element, Renderer renderer)
	throws RenderingException {
		renderer.endDirections();
	}
}
