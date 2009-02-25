package com.knitml.renderer.visitor.definition.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Instruction;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.controller.InstructionController;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class InstructionVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InstructionVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		return false;
	}

	public void end(Object element, RenderingContext context)
			throws RenderingException {
		Instruction instruction = (Instruction) element;
		String label = ContextUtils.deriveLabel(instruction,
				context.getPatternRepository());
		Instruction instructionToUse = context.getKnittingContext()
				.getPatternRepository().getBlockInstruction(
						instruction.getId());
		context.getRenderer().beginInstructionDefinition(instructionToUse, label);
		InstructionController embeddedController = new InstructionController(getVisitorFactory());
		embeddedController.visitInstruction(instructionToUse, context);
		context.getRenderer().endInstruction();

		context.getPatternRepository().addGlobalInstruction(instructionToUse, label);
	}

}
