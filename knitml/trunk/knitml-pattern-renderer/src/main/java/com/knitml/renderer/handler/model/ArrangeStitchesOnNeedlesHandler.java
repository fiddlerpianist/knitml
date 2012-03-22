package com.knitml.renderer.handler.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.block.ArrangeStitchesOnNeedles;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class ArrangeStitchesOnNeedlesHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(ArrangeStitchesOnNeedlesHandler.class);

	public boolean begin(Object event, Renderer renderer)
			throws RenderingException {
		ArrangeStitchesOnNeedles operation = (ArrangeStitchesOnNeedles) event;
		renderer.renderArrangeStitchesOnNeedles(
				operation.getNeedles());
		return true;
	}

}
