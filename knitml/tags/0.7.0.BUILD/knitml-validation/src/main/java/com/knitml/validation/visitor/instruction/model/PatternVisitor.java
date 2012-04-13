package com.knitml.validation.visitor.instruction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.model.pattern.Pattern;
import com.knitml.core.model.pattern.Version;
import com.knitml.core.common.VersionNotSupportedException;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.impl.AbstractPatternVisitor;

public class PatternVisitor extends AbstractPatternVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(PatternVisitor.class);
 
	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		context.getListenerManager().fireBegin(element, context);
		Pattern pattern = (Pattern) element;
		if (!Version.isSupported(pattern)) {
			throw new VersionNotSupportedException(pattern.getVersion());
		}
		visitChild(pattern.getDirectives(), context);
		visitChild(pattern.getGeneralInformation(), context);
		visitChild(pattern.getSupplies(), context);
		visitChild(pattern.getDirections(), context);
		context.getListenerManager().fireEnd(element, context);
	}
	
}
