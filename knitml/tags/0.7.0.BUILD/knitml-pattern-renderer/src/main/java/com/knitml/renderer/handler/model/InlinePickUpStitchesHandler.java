package com.knitml.renderer.handler.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.InlinePickUpStitches;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;


public class InlinePickUpStitchesHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InlinePickUpStitchesHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		InlinePickUpStitches spec = (InlinePickUpStitches) element;
		renderer.renderPickUpStitches(spec);
		return true;
	}

}
