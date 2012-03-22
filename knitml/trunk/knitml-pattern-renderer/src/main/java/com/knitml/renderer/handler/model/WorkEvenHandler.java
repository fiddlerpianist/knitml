package com.knitml.renderer.handler.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.WorkEven;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class WorkEvenHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(WorkEvenHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		WorkEven workEven = (WorkEven)element;
		renderer.renderWorkEven(workEven);
		return true;
	}
}
