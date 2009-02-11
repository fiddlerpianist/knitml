package com.knitml.renderer.visitor.definition.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.MergePoint;
import com.knitml.core.common.MergeType;
import com.knitml.core.common.ValidationException;
import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.InlineOperation;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.InstructionRef;
import com.knitml.core.model.directions.block.MergedInstruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.core.model.directions.inline.Knit;
import com.knitml.core.model.directions.inline.Purl;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.PatternRepository;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;
import com.knitml.renderer.visitor.impl.DefaultNameResolver;

public class MergedInstructionVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(MergedInstructionVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		MergedInstruction mergedInstruction = (MergedInstruction) element;
		PatternRepository repository = context.getPatternRepository();
		if (mergedInstruction.getType() != MergeType.PHYSICAL) {
			throw new NotImplementedException(
					"Only physical merge types are supported at this time");
		}

		Instruction instruction = createNewInstruction(mergedInstruction,
				context.getPatternRepository());
		String label = ContextUtils.deriveLabel(instruction, repository); 
		context.getPatternRepository().addGlobalInstruction(instruction, label);
		// now go back to "normal" processing
		getVisitorFactory().pushNameResolver(
				new DefaultNameResolver("com.knitml.renderer.visitor.model"));
		try {
			context.getRenderer().beginInstructionDefinition(instruction, label);
			if (!instruction.hasRows()) {
				visitChild(instruction.getForEachRowInInstruction(), context);
			} else {
				visitChildren(instruction, context);
			}
			context.getRenderer().endInstructionDefinition();
		} finally {
			getVisitorFactory().popNameResolver();
		}

	}

	private Instruction createNewInstruction(
			MergedInstruction mergedInstruction, PatternRepository repository)
			throws ValidationException {
		List<InstructionRef> instructionRefs = mergedInstruction
				.getInstructions();

		// create a list of the actual instructions, walking through each
		// instruction and determining the target row size for the merged
		// instruction
		List<Instruction> instructions = new ArrayList<Instruction>(
				instructionRefs.size());
		int targetRowSize = 0;
		for (InstructionRef instructionRef : instructionRefs) {
			Identifiable identifiable = repository
					.getInstruction(instructionRef.getRef().getId()).getInstruction();
			if (!(identifiable instanceof Instruction)) {
				throw new ValidationException(
						"The instruction-refs can only refer to block instruction elements with rows (or physically merged merge-instructions");
			}
			Instruction instruction = (Instruction) identifiable;
			int currentRowSize = instruction.getRows().size();
			if (mergedInstruction.getMergePoint() == MergePoint.ROW) {
				// a ROW merge point means that all instructions must have the
				// same number of rows
				if (targetRowSize > 0 && targetRowSize != currentRowSize) {
					throw new ValidationException(
							"Row sizes must match for merging instructions at the row level");
				} else if (targetRowSize == 0) {
					// initialize the target row size
					targetRowSize = currentRowSize;
				}
			} else if (mergedInstruction.getMergePoint() == MergePoint.END) {
				// an END merge point simply means combine the instructions
				// together at the ends
				targetRowSize += instruction.getRows().size();
			}
			instructions.add(instruction);
		}
		List<Row> newRows = new ArrayList<Row>(targetRowSize);
		// now merge the results into one instruction
		if (mergedInstruction.getMergePoint() == MergePoint.END) {
			int i = 1;
			for (Instruction instruction : instructions) {
				for (Row row : instruction.getRows()) {
					Row newRow = new Row(row, row.getOperations());
					newRow.setNumber(i);
					i++;
					newRows.add(row);
				}
			}
		} else if (mergedInstruction.getMergePoint() == MergePoint.ROW) {
			// for each merged row, walk through the instruction refs and merge
			// into one
			for (int i = 0; i < targetRowSize; i++) {
				List<InlineOperation> newOperations = new ArrayList<InlineOperation>();
				InlineOperation lastOperation = null;
				for (Instruction instruction : instructions) {
					Row currentRow = instruction.getRows().get(i);
					List<InlineOperation> currentOperations = currentRow
							.getOperations();
					if (lastOperation != null
							&& currentOperations.get(0).equals(lastOperation)) {
						InlineOperation doubledOperation = null;
						// FIXME this inheritance thing is really ugly here,
						// because Purl has to be first
						if (lastOperation instanceof Purl) {
							Purl lastPurlOperation = (Purl) lastOperation;
							doubledOperation = new Purl(lastPurlOperation
									.getNumberOfTimes() * 2, lastPurlOperation
									.getYarnIdRef(), lastPurlOperation
									.getLoopToWork());
						} else if (lastOperation instanceof Knit) {
							Knit lastKnitOperation = (Knit) lastOperation;
							doubledOperation = new Knit(lastKnitOperation
									.getNumberOfTimes() * 2, lastKnitOperation
									.getYarnIdRef(), lastKnitOperation
									.getLoopToWork());
						}
						// we don't double anything other than knits and purls

						if (doubledOperation != null) {
							// remove the last operation, as we are about to
							// "double" it
							newOperations.remove(newOperations.size() - 1);
							// now add the doubled operation
							newOperations.add(doubledOperation);
							// and "remove" the first current operation from
							// this list (as it's already been doubled)
							currentOperations = currentOperations.subList(1,
									currentOperations.size());
						}
					}
					newOperations.addAll(currentOperations);
					lastOperation = currentOperations.get(currentOperations
							.size() - 1);
				}
				Row newRow = new Row(instructions.get(0).getRows().get(i),
						newOperations);
				newRow.setNumber(i+1);
				newRows.add(newRow);
			}
		} else {
			throw new ValidationException(
					"Merge point for a merged instruction was left unspecified");
		}
		return new Instruction(mergedInstruction.getId(), mergedInstruction
				.getLabel(), mergedInstruction.getMessageKey(), newRows);
	}

}
