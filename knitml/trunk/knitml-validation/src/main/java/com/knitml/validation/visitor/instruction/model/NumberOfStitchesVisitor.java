package com.knitml.validation.visitor.instruction.model;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.information.NumberOfStitches;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.common.InvalidStructureException;
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
					throw new InvalidStructureException(MessageFormat.format(
							Messages.getString("NumberOfStitchesVisitor.UNEXPECTED_STITCH_COUNT"), //$NON-NLS-1$
							expectedNumberOfStitches, actualNumberOfStitches));
				}
			} else if (numberOfStitches.isInform()) {
				numberOfStitches.setNumber(context.getEngine()
						.getTotalNumberOfStitchesInRow());
			}
		}
	}

}
