package com.knitml.validation.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class GlobalPointcuts {

	@Pointcut("execution(public * com.knitml.validation.engine.impl.DefaultKnittingEngine.*(..))")
	public void knittingEngineImplementors() {}	

	@Pointcut("execution(public * com.knitml.validation.visitor.ElementVisitor.visit(..))")
	public void visitMethodExecution() {}	
	
}
