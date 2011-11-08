package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.ValidationException;
import com.knitml.core.model.directions.inline.Repeat;
import com.knitml.core.model.directions.inline.Repeat.Until;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.common.NoGapFoundException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class RepeatVisitor extends AbstractPatternVisitor {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(RepeatVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		Repeat repeat = (Repeat) element;
		// assume one repeat
		// visitChildren(repeat, context);

		Until until = repeat.getUntil();
		if (until == null) {
			throw new ValidationException(
					"A repeat element must have an [until] attribute defined");
		}
		Integer value = repeat.getValue();
		switch (until) {
		case TIMES:
			validatePositive(value);
			handleTimes(value, repeat, context);
			break;
		case BEFORE_END:
			validatePositive(value);
			handleBeforeEnd(value, repeat, context);
			break;
		case BEFORE_GAP:
			validatePositive(value);
			handleBeforeGap(value, repeat, context);
			break;
		case BEFORE_MARKER:
			validatePositive(value);
			handleBeforeMarker(value, repeat, context);
			break;
		case END:
			validateNull(value);
			handleEnd(repeat, context);
			break;
		case MARKER:
			validateNull(value);
			handleMarker(repeat, context);
			break;
		default:
		}
	}

	private void validatePositive(Integer value) throws ValidationException {
		if (value == null) {
			throw new ValidationException(
					"Repeat instruction must have a value in this context");
		}
		if (value <= 0) {
			throw new ValidationException(
					"Repeat instruction must have a positive value attribute in this context");
		}
	}

	private void validateNull(Integer value) throws ValidationException {
		if (value != null) {
			throw new ValidationException(
					"Repeat instruction must not have a value in this context");
		}
	}

	private void handleMarker(Repeat repeat, KnittingContext context)
			throws KnittingEngineException {
		boolean replaying = false;
		while (getStitchesBeforeMarker(context) > 0) {
			visitChildren(repeat, context);
			if (!replaying) {
				replaying = true;
				context.getPatternState().setReplayMode(true);
			}
		}
		if (replaying) {
			context.getPatternState().setReplayMode(false);
		}
	}

	private void handleBeforeMarker(int target, Repeat repeat,
			KnittingContext context) throws KnittingEngineException {
		boolean replaying = false;
		while (getStitchesBeforeMarker(context) > target) {
			visitChildren(repeat, context);
			if (!replaying) {
				replaying = true;
				context.getPatternState().setReplayMode(true);
			}
		}
		if (replaying) {
			context.getPatternState().setReplayMode(false);
		}
	}

	private void handleEnd(Repeat repeat, KnittingContext context)
			throws KnittingEngineException {
		boolean replaying = false;
		while (getStitchesBeforeEnd(context) > 0) {
			visitChildren(repeat, context);
			if (!replaying) {
				replaying = true;
				context.getPatternState().setReplayMode(true);
			}
		}
		if (replaying) {
			context.getPatternState().setReplayMode(false);
		}
	}

	private void handleBeforeEnd(int target, Repeat repeat,
			KnittingContext context) throws KnittingEngineException {
		boolean replaying = false;
		while (getStitchesBeforeEnd(context) > target) {
			visitChildren(repeat, context);
			if (!replaying) {
				replaying = true;
				context.getPatternState().setReplayMode(true);
			}
		}
		if (replaying) {
			context.getPatternState().setReplayMode(false);
		}
	}

	private void handleBeforeGap(int target, Repeat repeat,
			KnittingContext context) throws NoGapFoundException,
			KnittingEngineException {
		boolean replaying = false;
		while (getStitchesBeforeGap(context) > target) {
			visitChildren(repeat, context);
			if (!replaying) {
				replaying = true;
				context.getPatternState().setReplayMode(true);
			}
		}
		if (replaying) {
			context.getPatternState().setReplayMode(false);
		}
	}

	private void handleTimes(int numberOfTimes, Repeat repeat,
			KnittingContext context) throws KnittingEngineException {
		boolean replaying = false;
		for (int i = 0; i < numberOfTimes; i++) {
			visitChildren(repeat, context);
			if (!replaying) {
				replaying = true;
				context.getPatternState().setReplayMode(true);
			}
		}
		if (replaying) {
			context.getPatternState().setReplayMode(false);
		}
	}

}
