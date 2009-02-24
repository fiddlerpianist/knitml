package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.InlinePickUpStitches;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;


public class InlinePickUpStitchesVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InlinePickUpStitchesVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		InlinePickUpStitches spec = (InlinePickUpStitches) element;
		context.getRenderer().renderPickUpStitches(spec);
		return true;
	}

}
