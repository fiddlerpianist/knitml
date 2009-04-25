package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.NoInstructionFoundException;
import com.knitml.core.common.ValidationException;
import com.knitml.core.model.directions.block.RepeatInstruction;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class RepeatInstructionHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(RepeatInstructionHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		RenderingContext context = renderer.getRenderingContext();
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
		instructionInfo.setKnittingShape(context.getEngine()
				.getKnittingShape());
		renderer.renderRepeatInstruction(repeatInstruction,
				instructionInfo);
		return true;
	}
}
