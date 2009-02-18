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
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class InstructionVisitor extends AbstractRenderingVisitor {

	private final static Logger log = LoggerFactory
			.getLogger(InstructionVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		PatternRepository repository = context.getPatternRepository();
		Instruction instruction = (Instruction) element;
		String id = instruction.getId();
		if (instruction.hasLabelOrMessageKey()) {
			repository.addGlobalInstruction(instruction, ContextUtils.deriveLabel(instruction, repository));
			log.info("Adding global instruction [{}] to the pattern repository",id);
		} else {
			repository.addLocalInstruction(instruction);
			log.info("Adding local instruction [{}] to the pattern repository",id);
		}
		InstructionInfo instructionInfo = repository.getInstruction(id);
		context.getPatternState().setCurrentInstructionInfo(instructionInfo);

		if (!instruction.hasRows()) {
			visitChild(instruction.getForEachRowInInstruction(), context);
		} else {
			visitChildren(instruction, context);
		}
		if (instructionInfo.getRowRange() != null) {
			setLastExpressedRowNumber(instructionInfo.getRowRange().getMaximumInteger(), context);
			// if setLastExpressedRowNumber resets the local instructions, this would also clear out
			// the currently executing instruction. We don't want that to happen, so add it back in.
			if (repository.getInstruction(id) == null) {
				repository.addLocalInstruction(instruction);
			}
		}
		context.getPatternState().clearCurrentInstructionInfo();
		context.getRenderer().endInstruction();
	}

}
