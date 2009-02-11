package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.Pattern;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class PatternVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(PatternVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		Pattern pattern = (Pattern) element;
		visitChild(pattern.getDirectives(), context);
		visitChild(pattern.getGeneralInformation(), context);
		visitChild(pattern.getSupplies(), context);
		visitChild(pattern.getDirections(), context);
	}

}
