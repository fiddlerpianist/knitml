package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Row;
import com.knitml.core.model.directions.inline.ApplyNextRow;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.InstructionHolder;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.PatternRepository;
import com.knitml.validation.context.PatternState;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class ApplyNextRowVisitor extends AbstractValidationVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(ApplyNextRowVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		PatternRepository repository = context.getPatternRepository();
		PatternState state = context.getPatternState();

		ApplyNextRow applyNextRow = (ApplyNextRow) element;
		String id = applyNextRow.getInstructionRef().getId();
		InstructionHolder holder = state.getInstructionInUse(id);
		if (holder == null) {
			state.useInstruction(repository.getBlockInstruction(id));
			holder = state.getInstructionInUse(id);
		}
		assert holder != null;

		// find the row in the PatternState object and execute the next row
		Row row = holder.getNextRow();
		// visit the children of this row, not the row itself
		visitChildren(row, context);
	}

}
