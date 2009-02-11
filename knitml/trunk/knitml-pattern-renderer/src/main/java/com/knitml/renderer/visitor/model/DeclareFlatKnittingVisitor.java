package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.Side;
import com.knitml.core.model.directions.block.DeclareFlatKnitting;
import com.knitml.engine.settings.Direction;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class DeclareFlatKnittingVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(DeclareFlatKnittingVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		context.getPatternState().setCurrentKnittingShape(KnittingShape.FLAT);
		DeclareFlatKnitting spec = (DeclareFlatKnitting) element;
		if (spec.getNextRowSide() == Side.WRONG) {
			context.getEngine().declareFlatKnitting(Direction.BACKWARDS);
		} else {
			context.getEngine().declareFlatKnitting(Direction.FORWARDS);
		}
		context.getRenderer().renderDeclareFlatKnitting((DeclareFlatKnitting)element);
	}

}
