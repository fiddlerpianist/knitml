package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.Purl;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class PurlVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(PurlVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		Purl purl = (Purl) element;
		context.getRenderer().renderPurl(purl);
		return true;
	}
}
