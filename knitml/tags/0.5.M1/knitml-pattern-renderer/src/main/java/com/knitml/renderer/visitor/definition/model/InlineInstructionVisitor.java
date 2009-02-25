package com.knitml.renderer.visitor.definition.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.controller.InstructionController;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class InlineInstructionVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InlineInstructionVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		return false;
	}

	public void end(Object element, RenderingContext context)
			throws RenderingException {
		InlineInstruction instruction = (InlineInstruction) element;
		String label = ContextUtils.deriveLabel(instruction,
				context.getPatternRepository());
		InlineInstruction instructionToUse = context.getKnittingContext()
				.getPatternRepository().getInlineInstruction(
						instruction.getId());
		context.getRenderer().beginInlineInstructionDefinition(instructionToUse, label);
		InstructionController embeddedController = new InstructionController(getVisitorFactory());
		embeddedController.visitInlineInstruction(instructionToUse, context);
		context.getRenderer().endInlineInstructionDefinition(instructionToUse);
	}

}
