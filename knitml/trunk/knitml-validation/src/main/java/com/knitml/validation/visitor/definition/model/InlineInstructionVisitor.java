package com.knitml.validation.visitor.definition.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.PatternRepository;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class InlineInstructionVisitor extends AbstractPatternVisitor {

	private final static Logger log = LoggerFactory
			.getLogger(InlineInstructionVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		PatternRepository repository = context.getPatternRepository();
		InlineInstruction instruction = (InlineInstruction)element;
		String id = instruction.getId();
		if (id != null && repository.getInlineInstruction(id) == null) {
			context.getPatternRepository().addInlineInstruction(id, instruction);
			log.info("Just added inline instruction ID [{}] to the pattern repository", id); //$NON-NLS-1$
		}
	}

}
