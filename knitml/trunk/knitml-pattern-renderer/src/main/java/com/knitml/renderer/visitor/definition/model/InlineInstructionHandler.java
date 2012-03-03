package com.knitml.renderer.visitor.definition.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.InlineInstruction;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractEventHandler;
import com.knitml.renderer.visitor.controller.InstructionController;

public class InlineInstructionHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InlineInstructionHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		return false;
	}

	@Override
	public void end(Object element, Renderer renderer)
			throws RenderingException {
		RenderingContext context = renderer.getRenderingContext();
		InlineInstruction instruction = (InlineInstruction) element;
		String label = ContextUtils.deriveLabel(instruction,
				context.getPatternRepository());
		InlineInstruction instructionToUse = context.getKnittingContext()
				.getPatternRepository().getInlineInstruction(
						instruction.getId());
		context.getPatternRepository().addInlineInstruction(instructionToUse);
		
		renderer.beginInlineInstructionDefinition(instructionToUse, label);
		InstructionController embeddedController = new InstructionController(getEventHandlerFactory());
		embeddedController.visitInlineInstruction(instructionToUse, renderer);
		renderer.endInlineInstructionDefinition(instructionToUse);
	}

}
