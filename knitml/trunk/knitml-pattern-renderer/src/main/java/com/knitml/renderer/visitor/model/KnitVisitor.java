package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.Knit;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class KnitVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(KnitVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		Knit knit = (Knit)element;
		context.getEngine().knit(knit.getNumberOfTimes());
		context.getRenderer().renderKnit(knit);
	}
}
