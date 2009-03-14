package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.Decrease;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class DecreaseHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(DecreaseHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		Decrease decrease = (Decrease) element;
		renderer.renderDecrease(decrease);
		return true;
	}
	
}
