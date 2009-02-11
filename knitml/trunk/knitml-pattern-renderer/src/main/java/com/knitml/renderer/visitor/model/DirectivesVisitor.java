package com.knitml.renderer.visitor.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.header.Directives;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;
import com.knitml.renderer.visitor.impl.DefaultNameResolver;

public class DirectivesVisitor extends AbstractRenderingVisitor {

	private final static Logger log = LoggerFactory
			.getLogger(DirectivesVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		Directives directives = (Directives) element;
		List<String> messageSources = directives.getMessageSources();
		if (messageSources != null && !messageSources.isEmpty()) {
			log
					.debug("Setting pattern message sources in the pattern repository to: "
							+ directives.getMessageSources());
			context.getPatternRepository().setPatternMessageSources(
					directives.getMessageSources());
		}
		visitInstructionDefinitions(directives, context);
	}

	protected void visitInstructionDefinitions(Directives directives,
			RenderingContext context) throws RenderingException {
		// a different package for analyzing instructions in a different context
		List<Object> instructionDefinitions = directives
				.getInstructionDefinitions();
		if (directives.getInstructionDefinitions() != null) {
			getVisitorFactory().pushNameResolver(
					new DefaultNameResolver(
							"com.knitml.renderer.visitor.definition.model"));
			try {
				context.getRenderer().beginInstructionDefinitions();
				for (Object instructionDefinition : instructionDefinitions) {
					visitChild(instructionDefinition, context);
				}
				context.getRenderer().endInstructionDefinitions();
			} finally {
				getVisitorFactory().popNameResolver();
			}
		}
	}

}
