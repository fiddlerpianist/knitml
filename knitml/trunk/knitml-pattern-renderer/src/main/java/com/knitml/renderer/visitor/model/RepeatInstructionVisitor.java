package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.NoInstructionFoundException;
import com.knitml.core.common.ValidationException;
import com.knitml.core.model.directions.block.RepeatInstruction;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class RepeatInstructionVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(RepeatInstructionVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		RepeatInstruction repeatInstruction = (RepeatInstruction) element;
		String instructionId = repeatInstruction.getRef().getId();
		if (instructionId == null) {
			throw new ValidationException(
					"A [ref] attribute must be specified on a [repeat-instruction] element");
		}
		InstructionInfo instructionInfo = context.getPatternRepository()
				.getInstruction(instructionId);
		if (instructionInfo == null) {
			throw new NoInstructionFoundException("The instruction ["
					+ instructionId
					+ "] could not be found in the pattern repository");
		}
		instructionInfo.setKnittingShape(context.getPatternState()
				.getCurrentKnittingShape());
		context.getRenderer().renderRepeatInstruction(repeatInstruction,
				instructionInfo);
	}
}
