package com.knitml.validation.visitor.instruction;

public interface NameResolver {

	Class<Visitor> findVisitingClassFromClassName(Object object)
			throws ClassNotFoundException;

}