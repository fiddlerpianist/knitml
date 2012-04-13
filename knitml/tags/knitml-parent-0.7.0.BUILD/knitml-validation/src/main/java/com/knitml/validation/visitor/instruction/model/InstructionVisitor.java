package com.knitml.validation.visitor.instruction.model;

import static com.knitml.validation.visitor.util.InstructionUtils.createExpandedRows;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.block.Instruction;
import com.knitml.core.model.operations.block.Row;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.common.InvalidStructureException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.PatternRepository;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class InstructionVisitor extends AbstractPatternVisitor {

	private final static Logger log = LoggerFactory
			.getLogger(InstructionVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		// note that this visitor ignores any ChartInfo element which may be present in the instruction.
		// This is because this portion of the document is only used if a chart is going to be rendered.
		PatternRepository repository = context.getPatternRepository();
		Instruction instruction = (Instruction)element;
		String id = instruction.getId();
		context.getPatternState().setAsCurrent(instruction);
		if (repository.getBlockInstruction(id) == null) {
			Instruction instructionToAdd = createExpandedRows(instruction);
			if (!instructionToAdd.hasLabelOrMessageKey()) {
				if (instructionToAdd.hasRows()) {
					Row firstRow = instructionToAdd.getRows().get(0);
					if (firstRow.getAssignRowNumber() != null && firstRow.getAssignRowNumber() == false) {
						throw new InvalidStructureException("Instructions with unassigned rows numbers must have either a label or a message key"); //$NON-NLS-1$
					}
				}
				context.getPatternRepository().addLocalBlockInstruction(id, instructionToAdd);
				log.info("Just added local instruction [{}] to the pattern repository", id); //$NON-NLS-1$
			} else {
				context.getPatternRepository().addGlobalBlockInstruction(id, instructionToAdd);
				log.info("Just added global instruction [{}] to the pattern repository", id); //$NON-NLS-1$
			}
			// don't use the original instruction passed in for tracing; use the one with the expanded rows
			context.getPatternState().clearCurrentInstruction();
			context.getPatternState().setAsCurrent(instructionToAdd);
		}
		if (!instruction.hasRows()) {
			visitChild(instruction.getForEachRowInInstruction(), context);
		} else {
			Set<Row> rowsPlayed = new HashSet<Row>();
			Instruction instructionToUse = repository.getBlockInstruction(id);
			for (Row row : instructionToUse.getRows()) {
				if (rowsPlayed.contains(row)) {
					context.getPatternState().setReplayMode(true);
					visitChild(row, context);
					context.getPatternState().setReplayMode(false);
				} else {
					visitChild(row, context);
				}
				rowsPlayed.add(row);
			}
		}
		context.getPatternState().clearCurrentInstruction();
	}

}
