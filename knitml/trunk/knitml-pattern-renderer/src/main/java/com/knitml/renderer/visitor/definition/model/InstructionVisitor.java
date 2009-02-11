package com.knitml.renderer.visitor.definition.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Instruction;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.PatternRepository;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;
import com.knitml.renderer.visitor.impl.DefaultNameResolver;

public class InstructionVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InstructionVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		Instruction instruction = (Instruction) element;
		PatternRepository repository = context.getPatternRepository();
		context.getPatternRepository().addGlobalInstruction(instruction, ContextUtils.deriveLabel(instruction, repository));

		// now go back to "normal" processing
		getVisitorFactory().pushNameResolver(
				new DefaultNameResolver(
						"com.knitml.renderer.visitor.model"));
		try {
			context.getRenderer().beginInstructionDefinition(instruction, ContextUtils.deriveLabel(instruction, context.getPatternRepository()));
			if (!instruction.hasRows()) {
				visitChild(instruction.getForEachRowInInstruction(), context);
			} else {
				visitChildren(instruction, context);
			}
			context.getRenderer().endInstructionDefinition();
		} finally {
			getVisitorFactory().popNameResolver();
		}
	
	}
	
}
