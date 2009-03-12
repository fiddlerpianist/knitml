package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.header.GeneralInformation;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class GeneralInformationVisitor extends AbstractRenderingEvent {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(GeneralInformationVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		// TODO how to handle depth?
		context.getRenderer().renderGeneralInformation((GeneralInformation)element);
		// don't process children; we've handled it here
		return false;
	}

}
