package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.block.ForEachRowInInstruction;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.PatternRepository;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class ForEachRowInInstructionVisitor extends AbstractPatternVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(ForEachRowInInstructionVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		PatternRepository repository = context.getPatternRepository();
		ForEachRowInInstruction eachRow = (ForEachRowInInstruction) element;
		Identifiable identifiable = eachRow.getRef();
		int numberOfRows = findNumberOfRows(identifiable, repository);
		// for each row in referenced instruction, visit the children
		for (int i = 0; i < numberOfRows; i++) {
			if (i > 0) {
				context.getPatternState().setReplayMode(true);
			}
			// TODO examine each target row (i.e. numbers, short rows, etc.)
			context.getEngine().startNewRow();
			visitChildren(eachRow, context);
			context.getEngine().endRow();
			if (i > 0) {
				context.getPatternState().setReplayMode(false);
			}
			// clears any "fixed" row information which have been applied in this row
			context.getPatternState().clearActiveRowsForInstructions();
		}
	}

	private int findNumberOfRows(Identifiable identifiable,
			PatternRepository repository) {
		// in case you have nested instruction refs
		Instruction candidate = repository.getBlockInstruction(identifiable.getId());
		while (!candidate.hasRows()) {
			candidate = repository.getBlockInstruction(candidate.getForEachRowInInstruction()
					.getRef().getId());
		}
		return candidate.getRows().size();
	}

}
