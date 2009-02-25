package com.knitml.renderer.visitor;


public interface VisitorFactory {

	RenderingVisitor findVisitorFromClassName(Object instance);
	void pushNameResolver(NameResolver nameResolver);
	NameResolver popNameResolver();

}
