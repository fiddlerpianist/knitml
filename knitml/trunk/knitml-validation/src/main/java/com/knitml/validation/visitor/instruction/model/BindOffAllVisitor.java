package com.knitml.validation.visitor.instruction.model;

import static com.knitml.core.common.Wise.PURLWISE;

import com.knitml.core.model.directions.inline.BindOffAll;
import com.knitml.engine.KnittingEngine;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.common.NotEnoughStitchesException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class BindOffAllVisitor extends AbstractValidationVisitor {

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		BindOffAll bindOffAll = (BindOffAll) element;
		int numberOfIterations = context.getEngine()
				.getStitchesRemainingInRow();
		for (int i = 0; i < numberOfIterations; i++) {
			performIterativeOperation(bindOffAll, context, i, numberOfIterations
					- i);
		}
		
		// now fasten last stitch off
		if (bindOffAll.isFastenOffLastStitch()) {
			context.getEngine().reverseSlip();
			context.getEngine().fastenOff(1);
		}
	}

	protected void performIterativeOperation(BindOffAll bindOff,
			KnittingContext context, int numberPerformed,
			int numberLeftToPerform) throws NotEnoughStitchesException {
		KnittingEngine engine = context.getEngine();
		if (bindOff.getType() == PURLWISE) {
			engine.purl();
		} else {
			engine.knit();
		}
		if (numberPerformed > 0) {
			engine.passPreviousStitchOver();
		}
	}

}
