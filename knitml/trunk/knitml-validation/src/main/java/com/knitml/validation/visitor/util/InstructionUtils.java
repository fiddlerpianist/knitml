package com.knitml.validation.visitor.util;

import static com.knitml.core.common.RowDefinitionScope.EVEN;
import static com.knitml.core.common.RowDefinitionScope.ODD;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.knitml.core.common.RowDefinitionScope;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.engine.common.UnexpectedRowNumberException;
import com.knitml.validation.common.InvalidStructureException;

public class InstructionUtils {

	public static Instruction createExpandedRows(Instruction instruction)
			throws InvalidStructureException {
		if (!instruction.hasRows()) {
			return instruction;
		} else {
			List<Row> rows = instruction.getRows();
			SortedMap<Integer, Row> rowNumberMap = new TreeMap<Integer, Row>();
			boolean emptyRowNumbersFound = false;
			for (Row row : rows) {
				if (row.getNumbers() != null) {
					if (emptyRowNumbersFound) {
						throw new InvalidStructureException(
								Messages.getString("InstructionUtils.CANNOT_MIX_UNDECLARED_WITH_DECLARED_ROWS")); //$NON-NLS-1$
					}
					for (Integer i : row.getNumbers()) {
						Row previousRowInMap = rowNumberMap.put(i, row);
						if (previousRowInMap != null) {
							throw new InvalidStructureException(
									Messages.getString("InstructionUtils.ROW_MUST_BE_WITHIN_INSTRUCTION")); //$NON-NLS-1$
						}
					}
					if (row.getSubsequent() != null
							&& row.getSubsequent() != RowDefinitionScope.NONE) {
						processSubsequentRowDefinition(row, instruction,
								rowNumberMap);
					}
				} else {
					emptyRowNumbersFound = true;
					if (!rowNumberMap.isEmpty()) {
						throw new InvalidStructureException(
								Messages.getString("InstructionUtils.CANNOT_MIX_UNDECLARED_WITH_DECLARED_ROWS")); //$NON-NLS-1$
					}
				}
			}
			if (rowNumberMap.isEmpty()) {
				// if rows aren't manually specified, don't alter the
				// instruction
				return instruction;
			}
			if (!areRowsConsecutive(rowNumberMap.keySet())) {
				throw new InvalidStructureException(
						Messages.getString("InstructionUtils.ROW_NUMBERS_NOT_CONSECUTIVE")); //$NON-NLS-1$
			}
			if (instruction.getRowCount() != null
					&& instruction.getRowCount() != rowNumberMap.size()) {
				throw new InvalidStructureException(
						Messages.getString("InstructionUtils.ROW_SUM_MISMATCH")); //$NON-NLS-1$
			}

			// get all values from the map into a list of rows; use that list of
			// rows for processing
			List<Row> newRows = new ArrayList<Row>(rowNumberMap.size());
			newRows.addAll(rowNumberMap.values());
			return new Instruction(instruction, newRows);
		}
	}

	private static void processSubsequentRowDefinition(Row row,
			Instruction instruction, Map<Integer, Row> rowNumberMap) {
		// do subsequent row processing here
		if (instruction.getRowCount() == null) {
			throw new InvalidStructureException(
					Messages.getString("InstructionUtils.NO_ROW_COUNT_SUPPLIED")); //$NON-NLS-1$
		}

		// get the last row number for this definition
		int lastRowNumberForThisDefinition = row.getNumbers()[row.getNumbers().length - 1];
		// try to assume that the next row number would be two
		// plus the last row number
		int nextRowNumber = lastRowNumberForThisDefinition + 2;
		if ((isEven(lastRowNumberForThisDefinition) && row.getSubsequent() == ODD)
				|| (isOdd(lastRowNumberForThisDefinition) && row
						.getSubsequent() == EVEN)) {
			// in case someone specifies
			// "number=[2 3 4], subsequent='odd'" or something
			// strange like that
			nextRowNumber = lastRowNumberForThisDefinition + 1;
		}
		while (nextRowNumber <= instruction.getRowCount()) {
			rowNumberMap.put(nextRowNumber, row);
			nextRowNumber += 2;
		}
	}

	private static boolean isEven(int lastRowNumber) {
		return lastRowNumber % 2 == 0;
	}

	private static boolean isOdd(int lastRowNumber) {
		return !isEven(lastRowNumber);
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

	public static void validateInstructionAsGlobal(Instruction instruction)
			throws UnexpectedRowNumberException, InvalidStructureException {
		if (instruction.getLabel() == null
				&& instruction.getMessageKey() == null) {
			throw new InvalidStructureException(
					Messages.getString("InstructionUtils.GLOBAL_INSTRUCTION_EXPECTED")); //$NON-NLS-1$
		}
		if (instruction.getRows() != null && !instruction.getRows().isEmpty()) {
			Row firstRow = instruction.getRows().get(0);
			if (firstRow.getNumbers() != null && firstRow.getNumbers()[0] != 1) {
				throw new UnexpectedRowNumberException(1,
						firstRow.getNumbers()[0]);
			}
		}
	}

}
