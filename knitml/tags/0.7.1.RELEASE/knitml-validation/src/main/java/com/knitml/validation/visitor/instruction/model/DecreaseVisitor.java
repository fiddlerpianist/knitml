package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.DecreaseType;
import com.knitml.core.common.Wise;
import com.knitml.core.model.operations.StitchNature;
import com.knitml.core.model.operations.inline.Decrease;
import com.knitml.core.model.operations.inline.Slip;
import com.knitml.engine.KnittingEngine;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class DecreaseVisitor extends AbstractPatternVisitor {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(DecreaseVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		Decrease decrease = (Decrease) element;
		int numberOfStitches = decrease.getNumberOfTimes() == null ? 1
				: decrease.getNumberOfTimes();
		for (int i = 0; i < numberOfStitches; i++) {
			performIterativeOperation(decrease, context, i, numberOfStitches
					- i);
		}
	}

	protected void performIterativeOperation(Decrease decrease,
			KnittingContext context, int numberPerformed,
			int numberLeftToPerform) throws KnittingEngineException {
		KnittingEngine engine = context.getEngine();
		DecreaseType type = decrease.getType();
		if (type == null) {
			engine.knitTwoTogether();
			return;
		}
		switch (type) {
		case SSK:
			engine.slip(new Slip(2, Wise.KNITWISE, null, null));
			engine.reverseSlip();
			engine.reverseSlip();
			engine.knitTwoTogether(true);
			break;
		default:
			// otherwise rely on the nature of the stitch 
			if (decrease.getStitchNatureProduced() == StitchNature.PURL) {
				engine.purlTwoTogether();
			} else {
				engine.knitTwoTogether();
			}
		}
	}
}
