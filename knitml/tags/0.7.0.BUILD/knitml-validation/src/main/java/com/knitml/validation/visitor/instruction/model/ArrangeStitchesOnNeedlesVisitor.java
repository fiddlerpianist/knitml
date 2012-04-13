package com.knitml.validation.visitor.instruction.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.common.StitchesOnNeedle;
import com.knitml.core.model.operations.block.ArrangeStitchesOnNeedles;
import com.knitml.engine.Needle;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.common.WrongNumberOfNeedlesException;
import com.knitml.validation.common.InvalidStructureException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.NeedleNotFoundException;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;
import com.knitml.validation.visitor.util.NeedleUtils;

public class ArrangeStitchesOnNeedlesVisitor extends AbstractPatternVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(ArrangeStitchesOnNeedlesVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		ArrangeStitchesOnNeedles operation = (ArrangeStitchesOnNeedles)element;
		List<StitchesOnNeedle> stitchesOnNeedles = operation.getNeedles();
		int expectedNumberOfNeedles = context.getEngine().getNumberOfNeedles();
		if (stitchesOnNeedles.size() != expectedNumberOfNeedles) {
			throw new WrongNumberOfNeedlesException(expectedNumberOfNeedles, stitchesOnNeedles.size());
		}
		int[] stitchCountsOnNeedles = new int[expectedNumberOfNeedles];
		List<Needle> needlesInUse = context.getNeedlesInUse();
		for (StitchesOnNeedle stitchesOnNeedle : stitchesOnNeedles) {
			Needle needle = NeedleUtils.lookupNeedle(stitchesOnNeedle.getNeedle().getId(), context);
			if (needle == null) {
				throw new NeedleNotFoundException();
			}
			int position = needlesInUse.indexOf(needle);
			if (position != needlesInUse.lastIndexOf(needle)) {
				// this means the needle is in the list twice. This is not allowed!
				throw new InvalidStructureException(Messages.getString("ArrangeStitchesOnNeedlesVisitor.TOO_MANY_NEEDLES")); //$NON-NLS-1$
			}
			if (stitchesOnNeedle.getNumberOfStitches() == null) {
				throw new InvalidStructureException(Messages.getString("ArrangeStitchesOnNeedlesVisitor.NO_STITCH_COUNT_SPECIFIED")); //$NON-NLS-1$
			}
			stitchCountsOnNeedles[position] = stitchesOnNeedle.getNumberOfStitches();
		}
		context.getEngine().arrangeStitchesOnNeedles(stitchCountsOnNeedles);
	}


}
