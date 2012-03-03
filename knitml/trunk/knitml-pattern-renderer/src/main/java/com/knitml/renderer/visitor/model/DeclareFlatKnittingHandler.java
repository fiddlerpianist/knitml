package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.block.DeclareFlatKnitting;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class DeclareFlatKnittingHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(DeclareFlatKnittingHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		DeclareFlatKnitting spec = (DeclareFlatKnitting) element;
		renderer.renderDeclareFlatKnitting(spec);
		return true;
	}

}
