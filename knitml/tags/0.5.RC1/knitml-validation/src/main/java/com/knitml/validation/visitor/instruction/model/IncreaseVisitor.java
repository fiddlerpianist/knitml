package com.knitml.validation.visitor.instruction.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.IncreaseType;
import com.knitml.core.common.LoopToWork;
import com.knitml.core.model.directions.DiscreteInlineOperation;
import com.knitml.core.model.directions.inline.Increase;
import com.knitml.core.model.directions.inline.IncreaseIntoNextStitch;
import com.knitml.core.model.directions.inline.Knit;
import com.knitml.core.model.directions.inline.Purl;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class IncreaseVisitor extends AbstractPatternVisitor {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(IncreaseVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		Increase increase = (Increase) element;
		if (increase.getType() == IncreaseType.KFB) {
			// any increases which also involve working an existing stitch
			// should be done like this
			List<DiscreteInlineOperation> operations = new ArrayList<DiscreteInlineOperation>(2);
			operations.add(new Knit(1, null, LoopToWork.LEADING));
			operations.add(new Knit(1, null, LoopToWork.TRAILING));
			IncreaseIntoNextStitch calculatedIncrease = new IncreaseIntoNextStitch(
					increase.getYarnIdRef(), operations);
			context.getEngine().increase(calculatedIncrease);
		} else if (increase.getType() == IncreaseType.PFB) {
			// any increases which also involve working an existing stitch
			// should be done like this
			List<DiscreteInlineOperation> operations = new ArrayList<DiscreteInlineOperation>(2);
			operations.add(new Purl(1, null, LoopToWork.LEADING));
			operations.add(new Purl(1, null, LoopToWork.TRAILING));
			IncreaseIntoNextStitch calculatedIncrease = new IncreaseIntoNextStitch(
					increase.getYarnIdRef(), operations);
			context.getEngine().increase(calculatedIncrease);
		} else if (increase.getType() == IncreaseType.MOSS) {
			List<DiscreteInlineOperation> operations = new ArrayList<DiscreteInlineOperation>(2);
			operations.add(new Knit(1, null, LoopToWork.LEADING));
			operations.add(new Purl(1, null, LoopToWork.LEADING));
			IncreaseIntoNextStitch calculatedIncrease = new IncreaseIntoNextStitch(
					increase.getYarnIdRef(), operations);
			context.getEngine().increase(calculatedIncrease);
		} else {
			// all other increases do not work an existing stitch
			context.getEngine().increase(increase);
		}
	}

}
