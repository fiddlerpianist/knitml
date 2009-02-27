package com.knitml.renderer.visitor.model;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.impl.AbstractRenderingEvent;

public class InstructionRefVisitor extends AbstractRenderingEvent {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InstructionRefVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		// FIXME eventually, but bigger stuff to worry about right now
		throw new NotImplementedException("Not yet implemented - stay tuned!");
//		InstructionRef instructionRef = (InstructionRef) element;
//		InstructionInfo instructionInfo = context.getPatternRepository().getInstruction(instructionRef.getRef().getId());
//		visitChild(instructionInfo.getInstruction(), context);
	}
	
}
