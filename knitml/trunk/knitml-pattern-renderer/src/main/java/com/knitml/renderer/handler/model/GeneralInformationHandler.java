package com.knitml.renderer.handler.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.pattern.GeneralInformation;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class GeneralInformationHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(GeneralInformationHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		// TODO how to handle depth?
		renderer.renderGeneralInformation((GeneralInformation)element);
		// don't process children; we've handled it here
		return false;
	}

}
