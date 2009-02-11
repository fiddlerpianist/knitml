package com.knitml.renderer.visitor.definition.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.PatternRepository;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;
import com.knitml.renderer.visitor.impl.DefaultNameResolver;

public class InlineInstructionVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(InlineInstructionVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		InlineInstruction instruction = (InlineInstruction) element;
		PatternRepository repository = context.getPatternRepository();
		// will need to add when we do inline lookups (not supported yet)

		// now go back to "normal" processing
		getVisitorFactory().pushNameResolver(
				new DefaultNameResolver(
						"com.knitml.renderer.visitor.model"));
		try {
			context.getRenderer().beginInlineInstructionDefinition(instruction, ContextUtils.deriveLabel(instruction, repository));
			visitChildren(instruction, context);
			context.getRenderer().endInlineInstructionDefinition();
		} finally {
			getVisitorFactory().popNameResolver();
		}
	
	}
	
}
