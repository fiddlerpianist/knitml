package com.knitml.validation.visitor.definition.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.MergePoint;
import com.knitml.core.common.ValidationException;
import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.InlineOperation;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.InstructionRef;
import com.knitml.core.model.directions.block.MergedInstruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.core.model.directions.inline.Knit;
import com.knitml.core.model.directions.inline.Purl;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.common.InvalidStructureException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.PatternRepository;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class MergedInstructionVisitor extends AbstractPatternVisitor {

	private final static Logger log = LoggerFactory
			.getLogger(MergedInstructionVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		MergedInstruction mergedInstruction = (MergedInstruction) element;
		PatternRepository repository = context.getPatternRepository();
		String id = mergedInstruction.getId();
		// the instruction should not be in the repository yet
		if (repository.getBlockInstruction(id) != null) {
			throw new ValidationException("An instruction with ID [" + id
					+ "] has already been defined");
		}

		if (mergedInstruction.getLabel() == null
				&& mergedInstruction.getMessageKey() == null) {
			throw new InvalidStructureException(
					"Expected the merged instruction ["
							+ id
							+ "] to be a global instruction, which requires either a message-key or a label attribute. Neither attribute was found");
		}

		repository.addGlobalBlockInstruction(mergedInstruction.getId(),
				performPhysicalMerge(mergedInstruction, repository));
		log
				.info(
						"Just added instruction ID [{}] to the pattern repository",
						id);
	}

// TODO largely superfluous, or did this do something that the renderer's MergedInstructionVisitor didn't?
//	private Instruction createNewInstruction(
//			MergedInstruction mergedInstruction, PatternRepository repository)
//			throws InvalidStructureException {
//		List<InstructionRef> instructionRefs = mergedInstruction
//				.getInstructions();
//
//		// create a list of the actual instructions, walking through each
//		// instruction and determining
//		// the target row size for the merged instruction
//		List<Instruction> instructions = new ArrayList<Instruction>(
//				instructionRefs.size());
//		int targetRowSize = 0;
//		for (InstructionRef instructionRef : instructionRefs) {
//			Instruction instruction = repository
//					.getBlockInstruction(instructionRef.getRef().getId());
//			int currentRowSize = instruction.getRows().size();
//			if (mergedInstruction.getMergePoint() == MergePoint.ROW) {
//				// a ROW merge point means that all instructions must have the
//				// same number of rows
//				if (targetRowSize > 0 && targetRowSize != currentRowSize) {
//					throw new ValidationException(
//							"Row sizes must match for merging instructions at the row level");
//				} else if (targetRowSize == 0) {
//					// initialize the target row size
//					targetRowSize = currentRowSize;
//				}
//			} else if (mergedInstruction.getMergePoint() == MergePoint.END) {
//				// an END merge point simply means combine the instructions
//				// together at the ends
//				targetRowSize += instruction.getRows().size();
//			}
//			instructions.add(instruction);
//		}
//		List<Row> newRows = new ArrayList<Row>(targetRowSize);
//		// now merge the results into one instruction
//		if (mergedInstruction.getMergePoint() == MergePoint.END) {
//			for (Instruction instruction : instructions) {
//				for (Row row : instruction.getRows()) {
//					newRows.add(row);
//				}
//			}
//		} else if (mergedInstruction.getMergePoint() == MergePoint.ROW) {
//			for (int i = 0; i < targetRowSize; i++) {
//				List<InlineOperation> newOperations = new ArrayList<InlineOperation>();
//				for (Instruction instruction : instructions) {
//					Row currentRow = instruction.getRows().get(i);
//					newOperations.addAll(currentRow.getOperations());
//				}
//				Row newRow = new Row(instructions.get(0).getRows().get(i),
//						newOperations);
//				newRows.add(newRow);
//			}
//		} else {
//			throw new InvalidStructureException(
//					"Merge point for a merged instruction was left unspecified");
//		}
//		return new Instruction(mergedInstruction.getId(), mergedInstruction
//				.getLabel(), mergedInstruction.getMessageKey(), newRows);
//	}

	private Instruction performPhysicalMerge(MergedInstruction mergedInstruction,
			PatternRepository repository) throws ValidationException, InvalidStructureException {
		KnittingShape mergedInstructionShape = null;
		
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
					.getBlockInstruction(instructionRef.getRef().getId());
			if (!(identifiable instanceof Instruction)) {
				throw new ValidationException(
						"The instruction-refs can only refer to block instruction elements with rows (or physically merged merge-instructions");
			}
			Instruction instruction = (Instruction) identifiable;
			if (mergedInstructionShape == null) {
				mergedInstructionShape = instruction.getKnittingShape();
			} else if (mergedInstructionShape != instruction.getKnittingShape()) {
				throw new InvalidStructureException("Cannot merge instructions of different knitting shapes");
			}
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
					if (row.getNumbers() != null && row.getNumbers().length > 1) {
						throw new ValidationException("Cannot merge rows together with multiple row numbers per row element (not implemented at this time)");
					}
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
					if (currentRow.getNumbers() != null && currentRow.getNumbers().length > 1) {
						throw new ValidationException("Cannot merge rows together with multiple row numbers per row element (not implemented at this time)");
					}
					List<InlineOperation> currentOperations = currentRow
							.getOperations();
					if (lastOperation != null
							&& currentOperations.get(0).equals(lastOperation)) {
						InlineOperation doubledOperation = null;
						// Note that, because of inheritance, Purl has to be first
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
				newRow.setNumber(i + 1);
				newRows.add(newRow);
			}
		} else {
			throw new ValidationException(
					"Merge point for a merged instruction was left unspecified");
		}
		return new Instruction(mergedInstruction.getId(), mergedInstruction
				.getLabel(), mergedInstruction.getMessageKey(), mergedInstructionShape, newRows);
	}
}
