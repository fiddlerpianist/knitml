package com.knitml.renderer.handler.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.Increase;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class IncreaseHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(IncreaseHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		Increase increase = (Increase) element;
		renderer.renderIncrease(increase);
		return true;
	}
}
