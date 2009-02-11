package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Section;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;


public class SectionVisitor extends AbstractValidationVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(SectionVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		Section section = (Section) element;
		if (section.getResetRowCount()) {
			context.getEngine().resetRowNumber();
			context.getPatternRepository().clearLocalInstructions();
		}
		visitChildren(section, context);
	}

}
