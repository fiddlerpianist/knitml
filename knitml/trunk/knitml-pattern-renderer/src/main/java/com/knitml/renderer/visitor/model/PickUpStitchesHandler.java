package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.block.PickUpStitches;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;


public class PickUpStitchesHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(PickUpStitchesHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		PickUpStitches spec = (PickUpStitches) element;
		renderer.renderPickUpStitches(spec);
		return true;
	}

}
