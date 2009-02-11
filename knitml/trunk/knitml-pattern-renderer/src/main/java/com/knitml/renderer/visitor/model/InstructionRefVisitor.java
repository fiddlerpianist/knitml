package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.InstructionRef;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class InstructionRefVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InstructionRefVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		InstructionRef instructionRef = (InstructionRef) element;
		InstructionInfo instructionInfo = context.getPatternRepository().getInstruction(instructionRef.getRef().getId());
		visitChild(instructionInfo.getInstruction(), context);
	}
	
}
