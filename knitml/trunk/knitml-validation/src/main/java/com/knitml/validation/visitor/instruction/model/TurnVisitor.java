package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.operations.inline.Turn;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.common.WrongNumberOfStitchesException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class TurnVisitor extends AbstractPatternVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(TurnVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		Turn turn = (Turn) element;
		// only do an assertion and/or change the model if this is not currently being repeated
		if (!(context.getPatternState().isReplayMode())) {
			int actualStitchesLeft = context.getEngine()
					.getStitchesRemainingInRow();
			Integer expectedStitchesLeft = turn.getStitchesLeft();
			if (expectedStitchesLeft != null
					&& expectedStitchesLeft != actualStitchesLeft) {
				throw new WrongNumberOfStitchesException(expectedStitchesLeft,
						actualStitchesLeft);
			}
			if (turn.isInformUnworkedStitches() && expectedStitchesLeft == null) {
				turn.setStitchesLeft(actualStitchesLeft);
				turn.setInformUnworkedStitches(false);
			}
		}
		context.getEngine().turn();
	}
}
