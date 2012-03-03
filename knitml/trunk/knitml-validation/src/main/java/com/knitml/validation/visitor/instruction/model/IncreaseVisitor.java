package com.knitml.validation.visitor.instruction.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.IncreaseType;
import com.knitml.core.common.LoopToWork;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.Operation;
import com.knitml.core.model.operations.inline.Increase;
import com.knitml.core.model.operations.inline.Knit;
import com.knitml.core.model.operations.inline.Purl;
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
		
		
		// any increases which also involve working an existing stitch
		// should add to the specialOperations list
		List<DiscreteInlineOperation> substituteOperations = new ArrayList<DiscreteInlineOperation>(2);
		if (increase.getType() == IncreaseType.KFB) {
			substituteOperations.add(new Knit(1, null, LoopToWork.LEADING));
			substituteOperations.add(new Knit(1, null, LoopToWork.TRAILING));
		} else if (increase.getType() == IncreaseType.PFB) {
			substituteOperations.add(new Purl(1, null, LoopToWork.LEADING));
			substituteOperations.add(new Purl(1, null, LoopToWork.TRAILING));
		} else if (increase.getType() == IncreaseType.MOSS) {
			substituteOperations.add(new Knit(1, null, LoopToWork.LEADING));
			substituteOperations.add(new Purl(1, null, LoopToWork.LEADING));
		}
		
		if (!substituteOperations.isEmpty()) {
			int numberOfTimes = increase.getNumberOfTimes() == null ? 1 : increase.getNumberOfTimes();
			for (int i=0; i < numberOfTimes; i++) {
				context.getEngine().startWorkingIntoNextStitch();
				for (Operation op : substituteOperations) {
					if (op instanceof Purl) {
						context.getEngine().purl(((Purl)op).getLoopToWork() == LoopToWork.TRAILING ? true : false);
					} else {
						context.getEngine().knit(((Knit)op).getLoopToWork() == LoopToWork.TRAILING ? true : false);
					}
				}
				context.getEngine().endWorkingIntoNextStitch();
			}
		}
		else {
			// all other increases do not work an existing stitch
			context.getEngine().increase(increase);
		}
	}

}
