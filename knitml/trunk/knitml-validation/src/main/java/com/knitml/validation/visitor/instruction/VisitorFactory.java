package com.knitml.validation.visitor.instruction;



public interface VisitorFactory {
	
	Visitor findVisitorFromClassName(Object object);
	void pushNameResolver(NameResolver nameResolver);
	NameResolver popNameResolver();

}
