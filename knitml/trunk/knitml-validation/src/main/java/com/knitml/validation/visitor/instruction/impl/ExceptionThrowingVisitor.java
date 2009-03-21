package com.knitml.validation.visitor.instruction.impl;

import com.knitml.core.common.NoVisitorFoundException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.instruction.Visitor;

public class ExceptionThrowingVisitor implements Visitor {
	
	private Throwable t;
	
	public ExceptionThrowingVisitor() {
	}
	
	public ExceptionThrowingVisitor(Throwable cause) {
		this.t = cause;	
	}

	public void visit(Object operation, KnittingContext context) {
		throw new NoVisitorFoundException("No visitor found in classpath for [" + operation + "]: original exception " + t.getMessage(), t);
	}

}
