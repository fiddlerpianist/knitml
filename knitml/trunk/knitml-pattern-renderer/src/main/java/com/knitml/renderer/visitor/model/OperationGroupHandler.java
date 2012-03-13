package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.OperationGroup;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class OperationGroupHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(OperationGroupHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		// attempt to render the operation group together; if that fails, render the children individually
		boolean wasRendered = renderer.renderOperationGroup((OperationGroup)element);
		// if was not rendered, return (true meaning that the children should be processed)
		return !wasRendered;
	}
}
