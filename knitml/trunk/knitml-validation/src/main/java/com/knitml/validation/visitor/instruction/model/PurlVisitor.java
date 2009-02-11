package com.knitml.validation.visitor.instruction.model;

import com.knitml.core.model.directions.inline.Purl;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class PurlVisitor extends AbstractValidationVisitor {

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		Purl purl = (Purl)element;
		int count = purl.getNumberOfTimes() == null ? 1 : purl.getNumberOfTimes();
		context.getEngine().purl(count);
	}

}
