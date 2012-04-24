package com.knitml.renderer.handler.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.Repeat;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class RepeatHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(RepeatHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		Repeat repeat = (Repeat) element;
		renderer.beginRepeat(repeat);
		return true;
	}

	@Override
	public void end(Object element, Renderer renderer) {
		Repeat repeat = (Repeat) element;
		renderer.endRepeat(repeat.getUntil(), repeat.getValue());
	}
}
