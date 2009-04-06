package com.knitml.renderer.visitor.model;

import static com.knitml.renderer.context.ContextUtils.setLastExpressedRowNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Instruction;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.PatternRepository;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractEventHandler;
import com.knitml.renderer.visitor.controller.InstructionController;

public class InstructionHandler extends AbstractEventHandler {

	private final static Logger log = LoggerFactory
			.getLogger(InstructionHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		Object beforeVisitingEngineState = renderer.getRenderingContext()
				.getEngine().save();
		renderer.getRenderingContext().getPatternState().saveEngineState(
				beforeVisitingEngineState);
		return false;
	}

	@Override
	public void end(Object element, Renderer renderer) {
		RenderingContext context = renderer.getRenderingContext();
		// retrieve the "before" and "after" visiting states of the knitting
		// engine so that we have something to go back to
		Object beforeVisitingEngineState = context.getPatternState()
				.retrieveSavedEngineState();
		Object afterVisitingEngineState = context.getEngine().save();
		// start with the state before the instruction was sent through the
		// engine
		renderer.getRenderingContext().getEngine().restore(
				beforeVisitingEngineState);

		PatternRepository repository = context.getPatternRepository();
		Instruction instruction = (Instruction) element;
		String id = instruction.getId();
		Instruction candidateInstruction = context.getKnittingContext()
				.getPatternRepository()
				.getBlockInstruction(instruction.getId());

		Instruction instructionToUse = renderer
				.evaluateInstruction(candidateInstruction);

		if (instructionToUse == null) {
			instructionToUse = candidateInstruction;
		}
		InstructionInfo instructionInfo;
		if (instructionToUse.hasLabelOrMessageKey()) {
			instructionInfo = repository.addGlobalInstruction(instruction,
					ContextUtils.deriveLabel(instruction, repository));
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
			// would also clear out the currently executing instruction. We
			// don't want that to
			// happen, so add it back in.
			// FIXME this is really backwards; maybe we should redo this
			if (repository.getInstruction(instruction.getId()) == null) {
				repository
						.addLocalInstruction(instructionInfo.getInstruction());
			}
		}
		renderer.beginInstruction(instructionInfo);
		InstructionController embeddedController = new InstructionController(
				getEventHandlerFactory());
		embeddedController.visitInstruction(instructionToUse, renderer);
		context.getPatternState().clearCurrentInstructionInfo();
		renderer.endInstruction();

		// restore to the way things were (after engine processing but before
		// this method was called)
		renderer.getRenderingContext().getEngine().restore(
				afterVisitingEngineState);
	}

}
