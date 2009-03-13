package com.knitml.renderer.visitor.model;

import static com.knitml.renderer.context.ContextUtils.resetLastExpressedRowNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.InstructionGroup;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.context.PatternRepository;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class InstructionGroupHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InstructionGroupHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		RenderingContext context = renderer.getRenderingContext();
		InstructionGroup instructionGroup = (InstructionGroup) element;
		PatternRepository repository = context.getPatternRepository();
		String message = ContextUtils.deriveLabel(instructionGroup,
				repository);
		if (message != null) {
			renderer.beginInstructionGroup(message);
		} else {
			renderer.beginInstructionGroup();
		}
		if (instructionGroup.getResetRowCount()) {
			resetLastExpressedRowNumber(context);
		}
		return true;
	}
	
	public void end(Object element, Renderer renderer) {
		renderer.endInstructionGroup();
	}

}
