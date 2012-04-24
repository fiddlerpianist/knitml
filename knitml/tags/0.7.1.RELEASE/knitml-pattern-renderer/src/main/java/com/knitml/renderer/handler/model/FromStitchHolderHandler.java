package com.knitml.renderer.handler.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.FromStitchHolder;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class FromStitchHolderHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(FromStitchHolderHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		FromStitchHolder operation = (FromStitchHolder) element;
		renderer.beginFromStitchHolder(operation);
		return true;
	}
	
	@Override
	public void end(Object element, Renderer renderer) {
		FromStitchHolder operation = (FromStitchHolder) element;
		renderer.endFromStitchHolder(operation);
	}

}
