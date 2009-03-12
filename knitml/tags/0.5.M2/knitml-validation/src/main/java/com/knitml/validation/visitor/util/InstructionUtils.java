package com.knitml.validation.visitor.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.engine.common.UnexpectedRowNumberException;
import com.knitml.validation.common.InvalidStructureException;

public class InstructionUtils {

	public static Instruction createExpandedRows(
			Instruction instruction) throws InvalidStructureException {
		if (!instruction.hasRows()) {
			return instruction;
		} else {
			List<Row> rows = instruction.getRows();
			SortedMap<Integer, Row> rowNumberMap = new TreeMap<Integer, Row>();
			for (Row row : rows) {
				if (row.getNumbers() != null) {
					for (Integer i : row.getNumbers()) {
						rowNumberMap.put(i, row);
					}
				} else if (!rowNumberMap.isEmpty()) {
					throw new InvalidStructureException(
							"Cannot mix undeclared row numbers with declared row numbers");
				}
			}
			if (rowNumberMap.isEmpty()) {
				// if rows aren't manually specified, don't alter the instruction
				return instruction;
			}
			if (!areRowsConsecutive(rowNumberMap.keySet())) {
				throw new InvalidStructureException(
						"Row numbers must be consecutive in this instruction");
			}
			// get all values from the map into a list of rows; use that list of
			// rows for processing
			List<Row> newRows = new ArrayList<Row>(rowNumberMap.size());
			newRows.addAll(rowNumberMap.values());
			return new Instruction(instruction, newRows);
		}
	}

	/**
	 * Iterates through the collection to see if all Integers in the collection
	 * are consecutive. If the underlying collection's iterator's order is not
	 * predictable, calling this method will yield inconsistent results.
	 * 
	 * @param rowNumbers
	 * @return
	 */
	public static boolean areRowsConsecutive(Collection<Integer> rowNumbers) {
		if (rowNumbers.size() == 0) {
			return true;
		}
		int previous = rowNumbers.iterator().next();
		for (int i : rowNumbers) {
			if (i == previous) {
				continue;
			}
			if (i != (previous + 1)) {
				return false;
			}
			previous = i;
		}
		return true;
	}
	
	public static void validateInstructionAsGlobal(Instruction instruction) throws UnexpectedRowNumberException, InvalidStructureException {
		if (instruction.getLabel() == null && instruction.getMessageKey() == null) {
			throw new InvalidStructureException("Expected a global instruction here, which requires either a message-key or a label attribute. Neither attribute was found");
		}
		if (instruction.getRows() != null && !instruction.getRows().isEmpty()) {
			Row firstRow = instruction.getRows().get(0);
			if (firstRow.getNumbers() != null && firstRow.getNumbers()[0] != 1) {
				throw new UnexpectedRowNumberException(1, firstRow.getNumbers()[0]);
			}
		}
	}

}
