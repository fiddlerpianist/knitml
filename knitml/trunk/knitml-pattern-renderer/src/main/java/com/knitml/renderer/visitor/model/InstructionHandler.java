package com.knitml.renderer.visitor.model;

import static com.knitml.renderer.context.ContextUtils.deriveInstructionInfo;
import static com.knitml.renderer.context.ContextUtils.deriveLabel;
import static com.knitml.renderer.context.ContextUtils.setLastExpressedRowNumber;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.compatibility.Stack;
import com.knitml.core.model.operations.CompositeOperation;
import com.knitml.core.model.operations.Operation;
import com.knitml.core.model.operations.block.Instruction;
import com.knitml.core.model.operations.block.RepeatInstruction;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.PatternRepository;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractEventHandler;
import com.knitml.renderer.visitor.controller.InstructionController;

public class InstructionHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
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

		// retrieve the instruction from the knitting context's repository
		// rather than the instruction from the document, as the rows
		// may have been expanded.
		PatternRepository repository = context.getPatternRepository();
		Instruction instruction = (Instruction) element;
		String id = instruction.getId();
		Instruction candidateInstruction = context.getKnittingContext()
				.getPatternRepository().getBlockInstruction(id);

		// have the renderer evaluate the instruction and return a different
		// instruction if it wants to change anything. Pass it a sibling
		// repeat-instruction
		// if it can be found.
		Instruction instructionToUse = renderer.evaluateInstruction(
				candidateInstruction, findSiblingRepeatInstruction(instruction,
						renderer));
		// if the renderer didn't alter anything, it returns null
		if (instructionToUse == null) {
			instructionToUse = candidateInstruction;
		}

		InstructionInfo instructionInfo;
		if (instructionToUse.hasLabelOrMessageKey()) {
			instructionInfo = deriveInstructionInfo(instructionToUse,
					deriveLabel(instruction, repository));
			repository.addGlobalInstruction(instructionInfo);
		} else {
			instructionInfo = deriveInstructionInfo(instructionToUse, null);
			repository.addLocalInstruction(instructionInfo);
		}

		context.getPatternState().setCurrentInstructionInfo(instructionInfo);
		if (instructionInfo.getRowRange() != null) {
			setLastExpressedRowNumber(instructionInfo.getRowRange()
					.getMaximumInteger(), context);
			// if setLastExpressedRowNumber resets the local instructions, this
			// would also clear out the currently executing instruction. We
			// don't want that to happen, so add it back in.
			// FIXME this is really backwards; maybe we should redo this
			if (repository.getInstruction(instruction.getId()) == null) {
				repository.addLocalInstruction(instructionInfo);
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

	/**
	 * 
	 * @param instruction
	 *            the original instruction (as originally came in the object
	 *            model, not the one in the repository)
	 * @param renderer
	 * @return
	 */
	protected RepeatInstruction findSiblingRepeatInstruction(
			Instruction instruction, Renderer renderer) {
		Stack<CompositeOperation> operationTree = renderer
				.getRenderingContext().getPatternState().getOperationTree();
		if (!operationTree.empty()) {
			// get the parent... currently the only way to do this is to pop, peek, and push
			CompositeOperation me = operationTree.pop();
			CompositeOperation parent = null;
			try {
				if (operationTree.empty()) {
					return null;
				}
				parent = operationTree.peek();
			} finally {
				operationTree.push(me);
			}
			
			// get the siblings
			List<? extends Operation> siblings = parent.getOperations();
			int position = siblings.indexOf(instruction);
			if (position > 0 && siblings.size() > (position + 1)) {
				Operation nextOperation = siblings.get(position + 1);
				if (nextOperation instanceof RepeatInstruction) {
					RepeatInstruction repeatInstruction = ((RepeatInstruction) nextOperation);
					String instructionId = repeatInstruction.getRef().getId();
					if (instruction.getId().equals(instructionId)) {
						return repeatInstruction;
					}
				}
			}
		}
		return null;
	}

}
