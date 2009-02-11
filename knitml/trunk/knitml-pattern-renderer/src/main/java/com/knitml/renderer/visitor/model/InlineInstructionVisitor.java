package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class InlineInstructionVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InlineInstructionVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		InlineInstruction instruction = (InlineInstruction) element;
		visitChildren(instruction, context);
	}
	
}
