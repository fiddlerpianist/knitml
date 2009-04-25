package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.header.Gauge;
import com.knitml.core.model.header.GeneralInformation;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class GeneralInformationVisitor extends AbstractPatternVisitor {
	
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(GeneralInformationVisitor.class);

	public void visit(Object element, KnittingContext context) throws KnittingEngineException {
		GeneralInformation info = (GeneralInformation)element;
		Gauge gauge = info.getGauge();
		if (gauge != null) {
			visitChild(gauge, context);
		}
	}


}
