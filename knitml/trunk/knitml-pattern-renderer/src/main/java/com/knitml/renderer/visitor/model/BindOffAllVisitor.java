package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.BindOffAll;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;


public class BindOffAllVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(BindOffAllVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		context.getRenderer().renderBindOffAll((BindOffAll)element);
	}

}
