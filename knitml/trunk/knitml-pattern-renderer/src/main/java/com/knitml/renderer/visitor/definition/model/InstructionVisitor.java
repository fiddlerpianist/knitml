package com.knitml.renderer.visitor.definition.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Instruction;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.context.PatternRepository;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class InstructionVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InstructionVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		Instruction instruction = (Instruction) element;
		PatternRepository repository = context.getPatternRepository();
		context.getPatternRepository().addGlobalInstruction(instruction,
				ContextUtils.deriveLabel(instruction, repository));
		context.getRenderer().beginInstructionDefinition(
				instruction,
				ContextUtils.deriveLabel(instruction, context
						.getPatternRepository()));
		return true;
	}

	public void end(Object element, RenderingContext context)
			throws RenderingException {
		context.getRenderer().endInstructionDefinition();
	}

}
