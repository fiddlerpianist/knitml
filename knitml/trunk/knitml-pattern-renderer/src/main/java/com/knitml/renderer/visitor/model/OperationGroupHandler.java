package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class OperationGroupHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(OperationGroupHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		// there may need to be future logic here if we want to render stitch
		// groups differently?
		return true;
	}
}
