package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.CrossStitches;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class CrossStitchesHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(CrossStitchesHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		renderer.renderCrossStitches((CrossStitches)element);
		return true;
	}
}
