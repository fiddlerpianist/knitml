package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.Pattern;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class PatternHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(PatternHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		renderer.beginPattern((Pattern)element);
		return true;
	}

	@Override
	public void end(Object object, Renderer renderer)
			throws RenderingException {
		renderer.endPattern();
	}

}
