package com.knitml.validation.visitor.instruction.model;

import static com.knitml.core.common.Side.RIGHT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.Side;
import com.knitml.core.model.operations.block.DeclareFlatKnitting;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.settings.Direction;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class DeclareFlatKnittingVisitor extends AbstractPatternVisitor {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(DeclareFlatKnittingVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		DeclareFlatKnitting operation = (DeclareFlatKnitting)element;
		Side nextRowSide = operation.getNextRowSide();
		Direction nextRowDirection = Direction.BACKWARDS;
		if (nextRowSide == RIGHT) {
			nextRowDirection = Direction.FORWARDS;
		}
		context.getEngine().declareFlatKnitting(nextRowDirection);
	}

}
