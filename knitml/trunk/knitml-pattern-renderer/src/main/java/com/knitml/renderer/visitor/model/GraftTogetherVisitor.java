package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.GraftTogether;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class GraftTogetherVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(GraftTogetherVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		GraftTogether operation = (GraftTogether)element;
		context.getRenderer().renderGraftStitchesTogether(operation.getNeedles());
	}

}
