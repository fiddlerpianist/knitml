package com.knitml.renderer.handler.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.MultipleDecrease;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class MultipleDecreaseHandler extends AbstractEventHandler {
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(MultipleDecreaseHandler.class);

	public boolean begin(Object decrease, Renderer renderer)
			throws RenderingException {
		renderer.renderMultipleDecrease((MultipleDecrease)decrease);
		return true;
	}
}
