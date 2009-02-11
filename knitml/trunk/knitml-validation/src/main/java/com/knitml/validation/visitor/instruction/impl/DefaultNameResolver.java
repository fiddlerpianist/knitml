package com.knitml.validation.visitor.instruction.impl;

import com.knitml.validation.visitor.instruction.NameResolver;
import com.knitml.validation.visitor.instruction.Visitor;



public class DefaultNameResolver implements NameResolver {
	
	private static final String DEFAULT_PACKAGE_NAME = "com.knitml.validation.visitor.instruction.model";
	private String packageName = DEFAULT_PACKAGE_NAME;
	
	public DefaultNameResolver() {
	}
	
	public DefaultNameResolver(String packageName) {
		this.packageName = packageName;
	}
	
	/**
	 * @see com.knitml.validation.visitor.instruction.NameResolver#findVisitingClassFromClassName(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Class<Visitor> findVisitingClassFromClassName(Object object) throws ClassNotFoundException {
		return (Class<Visitor>)Class.forName(packageName + "." + object.getClass().getSimpleName() + "Visitor");
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
