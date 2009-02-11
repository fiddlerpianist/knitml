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

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		ForEachRowInInstruction forEachRow = (ForEachRowInInstruction) element;
		Range rowRange = deriveNewRowRange(forEachRow, context);
		InstructionInfo currentInstructionInfo = context.getPatternState().getCurrentInstructionInfo();
		if (currentInstructionInfo == null) {
			throw new RuntimeException("Expected a current instruction info to be in place for a ForEachRowInstruction operation");
		}
		currentInstructionInfo.setRowRange(rowRange);
		Row newRow = new Row(rowRange, forEachRow.getOperations());
		visitChild(newRow, context);
	}

	private Range deriveNewRowRange(ForEachRowInInstruction forEachRow, RenderingContext context) {
		PatternRepository repository = context.getPatternRepository();
		InstructionInfo targetInstructionInfo = repository.getInstruction(forEachRow.getRef().getId());
		if (targetInstructionInfo == null) {
			throw new ValidationException("Cannot locate instruction " + forEachRow.getRef().getId() + "; make sure you have validated the KnitML file");
		}
		Range originalRowRange = targetInstructionInfo.getRowRange();
		if (originalRowRange == null) {
			throw new ValidationException("Expecting row numbers defined for instruction [" + forEachRow.getRef().getId() + "]");
		}
		int lastRowNumber = context.getPatternState().getLastExpressedRowNumber();
		return new IntRange(originalRowRange.getMinimumInteger() + lastRowNumber, originalRowRange.getMaximumInteger() + lastRowNumber);
	}

}
