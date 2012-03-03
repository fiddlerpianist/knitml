package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.BindOffAll;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;


public class BindOffAllHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(BindOffAllHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		renderer.renderBindOffAll((BindOffAll)element);
		return true;
	}
}
