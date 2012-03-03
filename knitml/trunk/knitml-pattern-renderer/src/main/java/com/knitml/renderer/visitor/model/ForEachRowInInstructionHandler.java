package com.knitml.renderer.visitor.model;

import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.math.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.ValidationException;
import com.knitml.core.model.operations.block.ForEachRowInInstruction;
import com.knitml.core.model.operations.block.Row;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.PatternRepository;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class ForEachRowInInstructionHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(ForEachRowInInstructionHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		renderer.beginRow();
		return true;
	}

	@Override
	public void end(Object element, Renderer renderer) {
		ForEachRowInInstruction forEachRow = (ForEachRowInInstruction) element;
		Range rowRange = deriveNewRowRange(forEachRow, renderer.getRenderingContext());
		InstructionInfo currentInstructionInfo = renderer.getRenderingContext().getPatternState()
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
		renderer.endRow(newRow, renderer.getRenderingContext().getEngine().getKnittingShape());
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
