package com.knitml.renderer.visitor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.ValidationException;
import com.knitml.core.model.directions.inline.Repeat;
import com.knitml.core.model.directions.inline.Repeat.Until;
import com.knitml.engine.KnittingEngine;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.common.NoGapFoundException;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;

public class RepeatVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(RepeatVisitor.class);

	public void visit(Object element, RenderingContext context)
			throws RenderingException {
		Repeat repeat = (Repeat) element;
		context.getRenderer().beginRepeat(repeat);
		visitChildren(repeat, context);
		context.getRenderer().endRepeat(repeat.getUntil(), repeat.getValue());
	}

//	public void visit(Repeat repeat, KnittingEngine engine)
//			throws KnittingEngineException {
//		Until until = repeat.getUntil();
//		if (until == null) {
//			throw new ValidationException(
//					"A repeat element must have an [until] attribute defined");
//		}
//		Integer value = repeat.getValue();
//		switch (until) {
//		case TIMES:
//			handleTimes(value, repeat, engine);
//			break;
//		case BEFORE_END:
//			handleBeforeEnd(value, repeat, engine);
//			break;
//		case BEFORE_GAP:
//			handleBeforeGap(value, repeat, engine);
//			break;
//		case BEFORE_MARKER:
//			handleBeforeMarker(value, repeat, context);
//			break;
//		case END:
//			handleEnd(repeat, engine);
//			break;
//		case MARKER:
//			handleMarker(repeat, engine);
//			break;
//		default:
//		}
//	}
//
//	private void handleMarker(Repeat repeat,
//			KnittingEngine engine) throws KnittingEngineException {
//		while (engine.getStitchesToNextMarker() > 0) {
//			engine.slip();
//		}
//	}
//
//	private void handleBeforeMarker(int target, Repeat repeat,
//			KnittingEngine engine) throws KnittingEngineException {
//		while (engine.getStitchesToNextMarker() > target) {
//			engine.slip();
//		}
//	}
//
//	private void handleEnd(Repeat repeat, KnittingContext context)
//			throws KnittingEngineException {
//		while (getStitchesBeforeEnd(context) > 0) {
//			visitChildren(repeat, context);
//		}
//	}
//
//	private void handleBeforeEnd(int target, Repeat repeat,
//			KnittingContext context) throws KnittingEngineException {
//		while (getStitchesBeforeEnd(context) > target) {
//			visitChildren(repeat, context);
//		}
//	}
//
//	private void handleBeforeGap(int target, Repeat repeat,
//			KnittingContext context) throws NoGapFoundException,
//			KnittingEngineException {
//		while (getStitchesBeforeGap(context) > target) {
//			visitChildren(repeat, context);
//		}
//	}
//
//	private void handleTimes(int numberOfTimes, Repeat repeat,
//			KnittingContext context) throws KnittingEngineException {
//		for (int i = 0; i < numberOfTimes; i++) {
//			visitChildren(repeat, context);
//		}
//	}

}
