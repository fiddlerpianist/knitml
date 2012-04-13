package com.knitml.renderer.handler.model;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.renderer.Renderer;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.event.impl.AbstractEventHandler;

public class InstructionRefHandler extends AbstractEventHandler {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InstructionRefHandler.class);

	public boolean begin(Object element, Renderer renderer)
			throws RenderingException {
		// FIXME eventually, but bigger stuff to worry about right now
		throw new NotImplementedException("Not yet implemented - stay tuned!");
//		InstructionRef instructionRef = (InstructionRef) element;
//		InstructionInfo instructionInfo = context.getPatternRepository().getInstruction(instructionRef.getRef().getId());
//		visitChild(instructionInfo.getInstruction(), context);
	}
	
}
