package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.directions.information.NumberOfStitches;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class NumberOfStitchesVisitor extends AbstractPatternVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(NumberOfStitchesVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		if (!(context.getPatternState().isReplayMode())) {
			NumberOfStitches numberOfStitches = (NumberOfStitches) element;
			if (numberOfStitches.getNumber() != null) {
				int expectedNumberOfStitches = numberOfStitches.getNumber();
				int actualNumberOfStitches = context.getEngine()
						.getTotalNumberOfStitchesInRow();
				if (expectedNumberOfStitches != actualNumberOfStitches) {
					throw new KnittingEngineException("Expected "
							+ expectedNumberOfStitches + " stitches but was "
							+ actualNumberOfStitches);
				}
			} else if (numberOfStitches.isInform()) {
				numberOfStitches.setNumber(context.getEngine()
						.getTotalNumberOfStitchesInRow());
			}
		}
	}

}
