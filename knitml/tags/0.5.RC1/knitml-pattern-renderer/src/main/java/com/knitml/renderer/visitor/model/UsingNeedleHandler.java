package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.UsingNeedle;
import com.knitml.core.model.header.Needle;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class UsingNeedleHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(UsingNeedleHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		UsingNeedle operation = (UsingNeedle) element;
		Needle needle = operation.getNeedle();
		if (needle == null) {
			throw new RenderingException("Cannot find needle for using-needle operation in the pattern repository");
		}
		renderer.beginUsingNeedle(needle);
		return true;
	}
	
	@Override
	public void end(Object element, Renderer renderer) {
		renderer.endUsingNeedle();
	}

}
