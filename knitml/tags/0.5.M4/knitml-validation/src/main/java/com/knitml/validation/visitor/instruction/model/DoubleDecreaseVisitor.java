package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.DecreaseType;
import com.knitml.core.model.directions.inline.DoubleDecrease;
import com.knitml.engine.KnittingEngine;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class DoubleDecreaseVisitor extends AbstractPatternVisitor {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(DoubleDecreaseVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		DoubleDecrease decrease = (DoubleDecrease)element;
		int numberOfStitches = decrease.getNumberOfTimes() == null ? 1 : decrease.getNumberOfTimes(); 
		for (int i = 0; i < numberOfStitches; i++) {
			performIterativeOperation(decrease, context, i, numberOfStitches - i);
		}
	}
	
	protected void performIterativeOperation(DoubleDecrease decrease,
			KnittingContext context, int numberPerformed, int numberLeftToPerform) throws KnittingEngineException {
		// TODO for now, a decrease is just a k2tog on the engine
		KnittingEngine engine = context.getEngine();
		DecreaseType type = decrease.getType();
		if (type != null && type.equals(DecreaseType.CDD)) {
			engine.slip();
			engine.knitTwoTogether(true);
			engine.passPreviousStitchOver();
		} else {
			engine.knitThreeTogether();
		}
	}


}
