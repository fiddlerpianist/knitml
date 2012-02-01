package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.block.Section;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;


public class SectionVisitor extends AbstractPatternVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(SectionVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		Section section = (Section) element;
		section.setNumber(context.getPatternState().nextAvailableSectionNumber());
		context.getPatternState().setAsCurrent(section);
		if (section.getResetRowCount()) {
			context.getEngine().resetRowNumber();
			context.getPatternRepository().clearLocalInstructions();
		}
		visitChildren(section, context);
	}

}
