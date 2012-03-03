package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.Slip;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class SlipHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(SlipHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		Slip slip = (Slip) element;
		renderer.renderSlip(slip);
		return true;
	}
}
