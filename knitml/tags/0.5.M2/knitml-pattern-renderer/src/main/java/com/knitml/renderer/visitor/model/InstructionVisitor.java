package com.knitml.renderer.visitor.model;

import static com.knitml.renderer.context.ContextUtils.setLastExpressedRowNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Instruction;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.PatternRepository;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;
import com.knitml.renderer.visitor.controller.InstructionController;

public class InstructionVisitor extends AbstractRenderingEvent {

	private final static Logger log = LoggerFactory
			.getLogger(InstructionVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		return false;
	}

	public void end(Object element, RenderingContext context) {
		PatternRepository repository = context.getPatternRepository();
		Instruction instruction = (Instruction) element;
		String id = instruction.getId();
		Instruction candidateInstruction = context.getKnittingContext()
				.getPatternRepository()
				.getBlockInstruction(instruction.getId());
		Instruction instructionToUse = context.getRenderer().evaluateInstruction(candidateInstruction);
		if (instructionToUse == null) {
			instructionToUse = candidateInstruction;
		}
		InstructionInfo instructionInfo;
		if (instructionToUse.hasLabelOrMessageKey()) {
			instructionInfo = repository.addGlobalInstruction(instruction, ContextUtils
					.deriveLabel(instruction, repository));
			log.info(
					"Adding global instruction [{}] to the pattern repository",
					id);
		} else {
			instructionInfo = repository.addLocalInstruction(instruction);
			log.info("Adding local instruction [{}] to the pattern repository",
					id);
		}
		context.getPatternState().setCurrentInstructionInfo(instructionInfo);
		if (instructionInfo.getRowRange() != null) {
			setLastExpressedRowNumber(instructionInfo.getRowRange()
					.getMaximumInteger(), context);
			// if setLastExpressedRowNumber resets the local instructions, this
			// would also clear out the currently executing instruction. We don't want that to
			// happen, so add it back in.
			// FIXME this is really backwards; maybe we should redo this
			if (repository.getInstruction(instruction.getId()) == null) {
				repository.addLocalInstruction(instructionInfo.getInstruction());
			}
		}
		context.getRenderer().beginInstruction(instructionInfo);
		InstructionController embeddedController = new InstructionController(getEventFactory());
		embeddedController.visitInstruction(instructionToUse, context);
		context.getPatternState().clearCurrentInstructionInfo();
		context.getRenderer().endInstruction();
	}

}
