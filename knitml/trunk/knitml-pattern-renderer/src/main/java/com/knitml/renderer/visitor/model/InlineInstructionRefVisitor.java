package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.core.model.directions.inline.InlineInstructionRef;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class InlineInstructionRefVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InlineInstructionRefVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		// will have to use the repository for imported inline instructions
		// (when that is supported)
		InlineInstructionRef instructionRef = (InlineInstructionRef) element; 
		InlineInstruction instruction = instructionRef.getReferencedInstruction();
		String label = ContextUtils.deriveLabel(instruction, context.getPatternRepository());
		context.getRenderer().renderInlineInstructionRef(instructionRef, label);
		return true;
	}

}
