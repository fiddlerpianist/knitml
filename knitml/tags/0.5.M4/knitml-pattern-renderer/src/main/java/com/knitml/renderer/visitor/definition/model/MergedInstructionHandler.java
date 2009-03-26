package com.knitml.renderer.visitor.definition.model;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.MergeType;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.MergedInstruction;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractEventHandler;
import com.knitml.renderer.visitor.controller.InstructionController;

public class MergedInstructionHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(MergedInstructionHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		MergedInstruction mergedInstruction = (MergedInstruction) element;
		if (mergedInstruction.getType() != MergeType.PHYSICAL) {
			throw new NotImplementedException(
					"Only physical merge types are supported at this time");
		}
		// even though the validation visitor doesn't go into the model, make it
		// clear we don't want to, either
		return false;
	}

	public void end(Object element, Renderer renderer) {
		MergedInstruction mergedInstructionDefinition = (MergedInstruction) element;
		RenderingContext context = renderer.getRenderingContext();
		String label = ContextUtils.deriveLabel(mergedInstructionDefinition,
				context.getPatternRepository());
		Instruction candidateInstruction = context.getKnittingContext()
				.getPatternRepository().getBlockInstruction(
						mergedInstructionDefinition.getId());
		Instruction instructionToUse = renderer.evaluateInstructionDefinition(candidateInstruction);
		if (instructionToUse == null) {
			instructionToUse = candidateInstruction;
		}
		InstructionInfo instructionInfo = context.getPatternRepository().addGlobalInstruction(instructionToUse, label);
		renderer.beginInstructionDefinition(instructionInfo);
		InstructionController embeddedController = new InstructionController(getEventHandlerFactory());
		embeddedController.visitInstruction(candidateInstruction, renderer);
		renderer.endInstructionDefinition();
	}
}
