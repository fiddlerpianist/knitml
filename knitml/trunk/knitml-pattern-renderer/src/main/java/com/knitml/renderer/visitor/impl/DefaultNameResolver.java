package com.knitml.renderer.visitor.impl;

import com.knitml.renderer.visitor.NameResolver;
import com.knitml.renderer.visitor.RenderingVisitor;


public class DefaultNameResolver implements NameResolver {

	private static final String DEFAULT_PACKAGE_NAME = "com.knitml.renderer.visitor.model";
	private String packageName = DEFAULT_PACKAGE_NAME;
	
	public DefaultNameResolver() {
	}
	
	public DefaultNameResolver(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @see com.knitml.renderer.visitor.INameResolver#findVisitingClassFromClassName(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Class<RenderingVisitor> findVisitingClassFromClassName(Object object) throws ClassNotFoundException {
		return (Class<RenderingVisitor>)Class.forName(packageName + "." + object.getClass().getSimpleName() + "Visitor");
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
