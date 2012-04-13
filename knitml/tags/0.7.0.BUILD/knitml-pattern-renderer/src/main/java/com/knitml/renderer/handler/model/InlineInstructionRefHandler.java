package com.knitml.renderer.handler.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.InlineInstruction;
import com.knitml.core.model.operations.inline.InlineInstructionRef;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class InlineInstructionRefHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InlineInstructionRefHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		// will have to use the repository for imported inline instructions
		// (when that is supported)
		InlineInstructionRef instructionRef = (InlineInstructionRef) element; 
		InlineInstruction instruction = instructionRef.getReferencedInstruction();
		String label = ContextUtils.deriveLabel(instruction, renderer.getRenderingContext().getPatternRepository());
		renderer.renderInlineInstructionRef(instructionRef, label);
		return true;
	}

}
