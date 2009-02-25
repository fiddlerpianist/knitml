package com.knitml.renderer.visitor.model;

import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.math.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.ValidationException;
import com.knitml.core.model.directions.block.ForEachRowInInstruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.PatternRepository;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class ForEachRowInInstructionVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(ForEachRowInInstructionVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		context.getRenderer().beginRow();
		return true;
	}

	public void end(Object element, RenderingContext context) {
		ForEachRowInInstruction forEachRow = (ForEachRowInInstruction) element;
		Range rowRange = deriveNewRowRange(forEachRow, context);
		InstructionInfo currentInstructionInfo = context.getPatternState()
				.getCurrentInstructionInfo();
		if (currentInstructionInfo == null) {
			throw new RuntimeException(
					"Expected a current instruction info to be in place for a ForEachRowInstruction operation");
		}
		// this is simply resetting the row range to use in accordance with
		// where we are in the pattern
		currentInstructionInfo.setRowRange(rowRange);
		// this is simply resetting the row range to use in accordance with
		// where we are in the pattern
		Row newRow = new Row(currentInstructionInfo.getRowRange(),
				((ForEachRowInInstruction) element).getOperations());
		context.getRenderer().endRow(newRow, context.getPatternState().getCurrentKnittingShape());
	}

	private Range deriveNewRowRange(ForEachRowInInstruction forEachRow,
			RenderingContext context) {
		PatternRepository repository = context.getPatternRepository();
		InstructionInfo targetInstructionInfo = repository
				.getInstruction(forEachRow.getRef().getId());
		if (targetInstructionInfo == null) {
			throw new ValidationException("Cannot locate instruction "
					+ forEachRow.getRef().getId()
					+ "; make sure you have validated the KnitML file");
		}
		Range originalRowRange = targetInstructionInfo.getRowRange();
		if (originalRowRange == null) {
			throw new ValidationException(
					"Expecting row numbers defined for instruction ["
							+ forEachRow.getRef().getId() + "]");
		}
		int lastRowNumber = context.getPatternState()
				.getLastExpressedRowNumber();
		return new IntRange(originalRowRange.getMinimumInteger()
				+ lastRowNumber, originalRowRange.getMaximumInteger()
				+ lastRowNumber);
	}

}
