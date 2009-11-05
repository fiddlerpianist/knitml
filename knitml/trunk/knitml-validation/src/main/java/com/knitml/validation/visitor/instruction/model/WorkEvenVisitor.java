package com.knitml.validation.visitor.instruction.model;

import static com.knitml.core.model.directions.StitchNature.PURL;
import static com.knitml.core.model.directions.StitchNature.reverse;
import static com.knitml.engine.settings.Direction.BACKWARDS;

import com.knitml.core.model.directions.StitchNature;
import com.knitml.core.model.directions.inline.WorkEven;
import com.knitml.engine.KnittingEngine;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class WorkEvenVisitor extends AbstractPatternVisitor {

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		WorkEven operation = (WorkEven) element;
		KnittingEngine engine = context.getEngine();
		int count = operation.getNumberOfTimes() == null ? 1 : operation
				.getNumberOfTimes();
		for (int i = 0; i < count; i++) {
			StitchNature stitchOperation = engine.peekAtNextStitch()
					.getCurrentNature();
			if (engine.getDirection() == BACKWARDS) {
				stitchOperation = reverse(stitchOperation);
			}
			if (stitchOperation == PURL) {
				engine.purl();
			} else { // stitchOperation == KNIT
				engine.knit();
			}
		}
	}

}
