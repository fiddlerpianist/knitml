package com.knitml.validation.visitor.instruction.model;

import com.knitml.core.model.operations.inline.BindOff;
import com.knitml.engine.KnittingEngine;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.common.NotEnoughStitchesException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class BindOffVisitor extends AbstractPatternVisitor {

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		context.getEngine().bindOff((BindOff)element);

//		KnittingEngine engine = context.getEngine();
//		int numberOfIterations = bindOff.getNumberOfStitches();
//		for (int i = 0; i < numberOfIterations; i++) {
//			performIterativeOperation(bindOff, engine, i);
//		}
//		if (engine.getStitchesRemainingInRow() == 0) {
//			// last stitch on the row, so fasten it off
//			engine.reverseSlip();
//			engine.fastenOff(1);
//		} else {
//			performIterativeOperation(bindOff, engine, numberOfIterations);
//			// move one stitch back to the LH needle. This is because, in an instruction
//			// such as "k10, BO 10 sts, k10", the first k of the last k10 has actually
//			// already been worked as part of the bind off series. We cheat a bit and slip
//			// the stitch back onto the LH needle.
//			engine.reverseSlip();
//		}
//		
	}

	protected void performIterativeOperation(BindOff bindOff,
			KnittingEngine engine, int numberPerformed) throws NotEnoughStitchesException {
//		if (bindOff.getType() == PURLWISE) {
//			engine.purl();
//		} else {
//			engine.knit();
//		}
//		if (numberPerformed > 0) {
//			engine.passPreviousStitchOver();
//		}
	}

}
