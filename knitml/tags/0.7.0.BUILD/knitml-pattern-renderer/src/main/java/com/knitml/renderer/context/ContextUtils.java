package com.knitml.renderer.context;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.math.Range;

import com.knitml.core.model.common.Identifiable;
import com.knitml.core.model.operations.block.Instruction;
import com.knitml.core.model.operations.block.Row;

public class ContextUtils {
	
	public static String deriveLabel(Identifiable element, PatternRepository repository) {
		return repository.getPatternMessage(element.getMessageKey(), element.getLabel());
	}
	
	public static void setLastExpressedRowNumber(int newRowNumber, RenderingContext context) {
		int oldRowNumber = context.getPatternState().getLastExpressedRowNumber();
		if (newRowNumber < oldRowNumber) {
			context.getPatternRepository().clearLocalInstructions();
		}
		context.getPatternState().setLastExpressedRowNumber(newRowNumber);
	}

	public static void resetLastExpressedRowNumber(RenderingContext context) {
		context.getPatternRepository().clearLocalInstructions();
		context.getPatternState().resetLastExpressedRowNumber();
	}
	

	public static InstructionInfo deriveInstructionInfo(Instruction instruction, String label) {
		InstructionInfo instructionInfo = new InstructionInfo(instruction, label);
		if (instruction.getKnittingShape() != null) {
			// sync up the instruction info's shape with the shape declared in
			// the definition (if we know what the shape is)
			instructionInfo.setKnittingShape(instruction.getKnittingShape());
		}
		Range rowRange = deriveRowNumbers(instruction);
		if (rowRange != null) {
			instructionInfo.setRowRange(rowRange);
		}
		return instructionInfo;
	}
	
	private static Range deriveRowNumbers(Instruction instruction) {
		if (!instruction.hasRows()) {
			return null;
		}
		List<Row> rows = instruction.getRows();
		SortedSet<Integer> rowNumberSet = new TreeSet<Integer>();
		for (Row row : rows) {
			if (row.getNumbers() == null) {
				// if at any point we encounter a null row number, return null
				return null;
			} else {
				for (Integer i : row.getNumbers()) {
					rowNumberSet.add(i);
				}
			}
		}
		return new IntRange(rowNumberSet.first(), rowNumberSet.last());
	}
}
