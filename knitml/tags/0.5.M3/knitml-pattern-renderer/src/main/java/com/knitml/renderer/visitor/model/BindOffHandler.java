package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.BindOff;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;


public class BindOffHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(BindOffHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		renderer.renderBindOff((BindOff)element);
		return true;
	}

}
