package com.knitml.validation.visitor.instruction.model;

import static com.knitml.validation.visitor.util.InstructionUtils.createExpandedRows;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Instruction;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.PatternRepository;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class InstructionVisitor extends AbstractPatternVisitor {

	private final static Logger log = LoggerFactory
			.getLogger(InstructionVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		PatternRepository repository = context.getPatternRepository();
		Instruction instruction = (Instruction)element;
		String id = instruction.getId();
		if (repository.getBlockInstruction(id) == null) {
			Instruction instructionToAdd = createExpandedRows(instruction);
			if (!instructionToAdd.hasLabelOrMessageKey()) {
				context.getPatternRepository().addLocalBlockInstruction(id, instructionToAdd);
				log.info("Just added local instruction [{}] to the pattern repository", id);
			} else {
				context.getPatternRepository().addGlobalBlockInstruction(id, instructionToAdd);
				log.info("Just added global instruction [{}] to the pattern repository", id);
			}
		}
		context.getPatternState().setWithinInstruction(true);
		if (!instruction.hasRows()) {
			visitChild(instruction.getForEachRowInInstruction(), context);
		} else {
			visitChildren(repository.getBlockInstruction(id), context);
		}
		context.getPatternState().setWithinInstruction(false);
	}

}
