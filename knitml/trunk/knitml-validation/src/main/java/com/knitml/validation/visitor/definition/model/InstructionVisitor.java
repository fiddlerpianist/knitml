package com.knitml.validation.visitor.definition.model;

import static com.knitml.validation.visitor.util.InstructionUtils.createExpandedRows;
import static com.knitml.validation.visitor.util.InstructionUtils.validateInstructionAsGlobal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Instruction;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.PatternRepository;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class InstructionVisitor extends AbstractValidationVisitor {

	private final static Logger log = LoggerFactory
			.getLogger(InstructionVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		PatternRepository repository = context.getPatternRepository();
		Instruction instruction = (Instruction) element;
		String id = instruction.getId();
		if (repository.getBlockInstruction(id) == null) {
			Instruction instructionToAdd = createExpandedRows(instruction);
			validateInstructionAsGlobal(instructionToAdd);
			context.getPatternRepository().addGlobalBlockInstruction(id,
					instructionToAdd);
			log.info(
					"Just added instruction ID [{}] to the pattern repository",
					id);
		}
		if (instruction.hasRows()) {
			int headerRowNumber = 1;
			context.getPatternState().setHeaderRowNumber(headerRowNumber);
			// visit each row in the instruction to potentially add row numbers.
			// Visit the ORIGINAL instruction, not the one returned by
			// createExpandedRows or the instruction in the repository
			// so that we can mark up the original elements
			visitChildren(instruction, context);
		}
	}

}
