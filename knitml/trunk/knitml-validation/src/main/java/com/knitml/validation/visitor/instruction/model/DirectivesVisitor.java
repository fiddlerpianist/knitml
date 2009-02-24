package com.knitml.validation.visitor.instruction.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.header.Directives;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;
import com.knitml.validation.visitor.instruction.impl.DefaultNameResolver;

public class DirectivesVisitor extends AbstractPatternVisitor {
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(DirectivesVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		Directives directives = (Directives) element;
		visitInstructionDefinitions(directives, context);
	}

	protected void visitInstructionDefinitions(Directives directives,
			KnittingContext context) throws KnittingEngineException {
		// a different package for analyzing instructions in a different context
		List<Object> instructionDefinitions = directives
				.getInstructionDefinitions();
		if (directives.getInstructionDefinitions() != null) {
			pushNameResolver(new DefaultNameResolver(
					"com.knitml.validation.visitor.definition.model"));
			try {
				for (Object instructionDefinition : instructionDefinitions) {
					visitChild(instructionDefinition, context);
				}
			} finally {
				popNameResolver();
			}
		}
	}

}
