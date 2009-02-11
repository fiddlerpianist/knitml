package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.header.Gauge;
import com.knitml.core.model.header.GeneralInformation;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.Visitor;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;

public class GeneralInformationVisitor extends AbstractValidationVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(GeneralInformationVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		GeneralInformation info = (GeneralInformation)element;
		Gauge gauge = info.getGauge();
		if (gauge != null) {
			Visitor gaugeVisitor = this.getVisitorFactory().findVisitorFromClassName(gauge);
			gaugeVisitor.visit(gauge, context);
		}
	}


}
