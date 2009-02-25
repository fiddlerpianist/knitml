package com.knitml.renderer.visitor;

public interface NameResolver {

	Class<RenderingVisitor> findVisitingClassFromClassName(Object object)
			throws ClassNotFoundException;

}