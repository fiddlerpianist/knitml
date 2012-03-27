package com.knitml.renderer.handler.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.IncreaseIntoNextStitch;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class IncreaseIntoNextStitchHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(IncreaseIntoNextStitchHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		IncreaseIntoNextStitch operation = (IncreaseIntoNextStitch) element;
		return renderer.beginIncreaseIntoNextStitch(operation);
	}

	public void end(Object element, Renderer renderer) throws RenderingException {
		IncreaseIntoNextStitch operation = (IncreaseIntoNextStitch) element;
		renderer.endIncreaseIntoNextStitch(operation);
	}
	
}
