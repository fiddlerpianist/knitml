package com.knitml.validation.visitor.instruction.impl;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;

import org.jibx.runtime.ITrackSource;
import org.springframework.aop.ThrowsAdvice;

import com.knitml.core.model.directions.Operation;
import com.knitml.engine.common.GenericKnittingEngineException;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;

public class ElementDescriptionAfterThrowingAdvice implements ThrowsAdvice {
	public void afterThrowing(Method method, Object[] args, Object target,
			KnittingEngineException ex) throws KnittingEngineException {
		if (method.getName().equals("visit") //$NON-NLS-1$
				&& ex.getLocationBreadcrumb() == null
				&& args[1] instanceof KnittingContext) {
			KnittingContext context = (KnittingContext) args[1];
			List<Object> breadcrumb = context.getPatternState().getLocationBreadcrumb();
			ex.setLocationBreadcrumb(breadcrumb);
			if (args[0] instanceof Operation) {
				ex.setOffendingOperation((Operation)args[0]);
			}

			// XML document information stored by JiBX
			if (args[0] instanceof ITrackSource
					&& ((ITrackSource) args[0]).jibx_getLineNumber() > 0) {
				ITrackSource source = (ITrackSource) args[0];
				String extraInfo = MessageFormat.format(Messages.getString("XML_DOCUMENT_POSITION"), source.jibx_getLineNumber(), source.jibx_getColumnNumber()); //$NON-NLS-1$
				ex.setAdditionalInformation(extraInfo);
			}
		}
		throw ex;
	}

	public void afterThrowing(Method method, Object[] args, Object target,
			Exception ex) throws KnittingEngineException {
		if (ex instanceof KnittingEngineException) {
			afterThrowing(method, args, target, (KnittingEngineException) ex);
		} else {
			afterThrowing(method, args, target, new GenericKnittingEngineException(ex));
		}
	}

}
