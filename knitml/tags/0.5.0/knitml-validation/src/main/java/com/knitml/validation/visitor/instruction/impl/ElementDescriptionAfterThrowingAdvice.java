package com.knitml.validation.visitor.instruction.impl;

import java.lang.reflect.Method;

import org.jibx.runtime.ITrackSource;
import org.springframework.aop.ThrowsAdvice;

import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;

public class ElementDescriptionAfterThrowingAdvice implements ThrowsAdvice {
	public void afterThrowing(Method method, Object[] args, Object target,
			KnittingEngineException ex) throws KnittingEngineException {
		if (method.getName().equals("visit")
				&& ex.getAdditionalInformation() == null) {
			if (args[0] instanceof ITrackSource
					&& args[1] instanceof KnittingContext) {
				ITrackSource source = (ITrackSource) args[0];
				KnittingContext context = (KnittingContext) args[1];
				if (!(context.getPatternState()).isReplayMode()) {
					StringBuffer sb = new StringBuffer();
					sb.append("line ").append(source.jibx_getLineNumber())
							.append(", column ").append(
									source.jibx_getColumnNumber()).append(
									" of XML document");
					ex.setAdditionalInformation(sb.toString());
					ex.setTotalRowNumber(context.getEngine()
							.getTotalRowsCompleted() + 1);
					ex.setLocalRowNumber(context.getEngine()
							.getCurrentRowNumber());
					ex.setInstructionRepeatCounts(context.getPatternState()
							.getInstructionRepeatCounts());
				}
			}
		}
		throw ex;
	}

	public void afterThrowing(Method method, Object[] args, Object target,
			Exception ex) throws KnittingEngineException {
		if (ex instanceof KnittingEngineException) {
			afterThrowing(method, args, target, (KnittingEngineException)ex);
		} else {
			afterThrowing(method, args, target, new KnittingEngineException(ex));
		}
	}


}
