package com.knitml.validation.visitor.instruction.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.common.Needle;
import com.knitml.core.model.operations.block.UseNeedles;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.NeedleNotFoundException;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;
import com.knitml.validation.visitor.util.NeedleUtils;

public class UseNeedlesVisitor extends AbstractPatternVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(UseNeedlesVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		UseNeedles operation = (UseNeedles)element;
		List<Needle> needleList = operation.getNeedles();
		List<com.knitml.engine.Needle> engineNeedleList = new ArrayList<com.knitml.engine.Needle>(needleList.size());
		for (Needle needle : needleList) {
			com.knitml.engine.Needle engineNeedle = NeedleUtils.lookupNeedle(needle.getId(), context);
			if (engineNeedle == null) {
				throw new NeedleNotFoundException(needle.getId());
			}
			engineNeedleList.add(engineNeedle);
		}
		context.getEngine().useNeedles(engineNeedleList);
		context.setNeedlesInUse(engineNeedleList);
	}



}
