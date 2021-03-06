package com.knitml.validation.visitor.instruction.model;

import com.knitml.core.model.operations.inline.Purl;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class PurlVisitor extends AbstractPatternVisitor {

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		Purl purl = (Purl)element;
		int count = purl.getNumberOfTimes() == null ? 1 : purl.getNumberOfTimes();
		context.getEngine().purl(count);
	}

}
