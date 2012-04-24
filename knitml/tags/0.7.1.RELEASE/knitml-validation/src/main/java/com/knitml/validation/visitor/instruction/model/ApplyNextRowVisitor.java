package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.block.InstructionHolder;
import com.knitml.core.model.operations.block.Row;
import com.knitml.core.model.operations.inline.ApplyNextRow;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.PatternRepository;
import com.knitml.validation.context.PatternState;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class ApplyNextRowVisitor extends AbstractPatternVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(ApplyNextRowVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		PatternRepository repository = context.getPatternRepository();
		PatternState state = context.getPatternState();
		ApplyNextRow applyNextRow = (ApplyNextRow) element;
		String id = applyNextRow.getInstructionRef().getId();

		// Find which instruction we are attempting to apply the next row of.
		// First, see if the row to apply has been "fixed" to the currently executing row
		Row rowToApply = state.getActiveRowForInstruction(id);
		if (rowToApply == null) {
			// this is the first occurrence of apply-next-row in this instruction
			InstructionHolder holder = state.getInstructionInUse(id);
			if (holder == null) {
				state.useInstruction(repository.getBlockInstruction(id));
				holder = state.getInstructionInUse(id);
			}
			assert holder != null;

			// find the row in the PatternState object and execute the next row
			rowToApply = holder.getNextRow();
			state.setActiveRowForInstruction(id, rowToApply);
		}

		
		assert rowToApply != null;
		// visit the children of this row, not the row itself. This is
		// considered a replay
		context.getPatternState().setReplayMode(true);
		visitChildren(rowToApply, context);
		context.getPatternState().setReplayMode(false);
	}

}
