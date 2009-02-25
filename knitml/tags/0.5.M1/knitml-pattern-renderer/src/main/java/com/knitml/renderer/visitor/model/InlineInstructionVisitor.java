package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

/**
 * 
 * @author Jonathan Whitall
 *
 */
public class InlineInstructionVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InlineInstructionVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		InlineInstruction instruction = (InlineInstruction) element;
		context.getPatternRepository().addInlineInstruction(instruction);
		// inline instructions not defined in the header do not have a label
		context.getRenderer().beginInlineInstruction(instruction);
		return true;
	}

	public void end(Object element, RenderingContext context) {
		context.getRenderer().endInlineInstruction((InlineInstruction)element);
	}

}
