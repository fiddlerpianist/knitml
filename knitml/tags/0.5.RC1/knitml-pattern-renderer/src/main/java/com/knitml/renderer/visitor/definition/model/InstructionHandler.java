package com.knitml.renderer.visitor.definition.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Instruction;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractEventHandler;
import com.knitml.renderer.visitor.controller.InstructionController;

public class InstructionHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InstructionHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		return false;
	}

	@Override
	public void end(Object element, Renderer renderer)
			throws RenderingException {
		RenderingContext context = renderer.getRenderingContext();
		Instruction instruction = (Instruction) element;
		String label = ContextUtils.deriveLabel(instruction,
				context.getPatternRepository());
		Instruction candidateInstruction = context.getKnittingContext()
				.getPatternRepository().getBlockInstruction(
						instruction.getId());
		Instruction instructionToUse = renderer.evaluateInstructionDefinition(candidateInstruction);
		if (instructionToUse == null) {
			instructionToUse = candidateInstruction;
		}
		InstructionInfo instructionInfo = context.getPatternRepository().addGlobalInstruction(instructionToUse, label);
		renderer.beginInstructionDefinition(instructionInfo);
		InstructionController embeddedController = new InstructionController(getEventHandlerFactory());
		embeddedController.visitInstruction(instructionToUse, renderer);
		renderer.endInstructionDefinition();

	}

}
