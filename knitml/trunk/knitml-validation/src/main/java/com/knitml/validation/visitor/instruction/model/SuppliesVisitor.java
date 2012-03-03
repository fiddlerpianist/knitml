package com.knitml.validation.visitor.instruction.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.knitml.core.model.common.Needle;
import com.knitml.core.model.common.StitchHolder;
import com.knitml.core.model.pattern.Supplies;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class SuppliesVisitor extends AbstractPatternVisitor {

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		
		Supplies supplies = (Supplies) element;
		List<Needle> needles = supplies.getNeedles();
		if (supplies.hasNeedles()) {
			// First, remove the "default" needle since we are defining needles
			context.getEngine().useNeedles(new ArrayList<com.knitml.engine.Needle>());
			
			// Now add visit each needle definition, which will add each needle to the repository
			for (Needle needle : needles) {
				visitChild(needle, context);
			}
			// Now see what's in the repository. If there is one needle, go ahead and use it.
			Collection<com.knitml.engine.Needle> engineNeedles = context.getPatternRepository().getNeedles();
			if (engineNeedles.size() == 1) {
				// if only one needle is defined, go ahead and use it with the engine
				List<com.knitml.engine.Needle> engineNeedlesAsList = new ArrayList<com.knitml.engine.Needle>(1);
				engineNeedlesAsList.addAll(engineNeedles);
				context.getEngine().useNeedles(engineNeedlesAsList);
				context.setNeedlesInUse(engineNeedlesAsList);
			}

		}
		if (supplies.hasAccessories()) {
			for (StitchHolder stitchHolder : supplies.getStitchHolders()) {
				visitChild(stitchHolder, context);
			}
		}
	}

}
