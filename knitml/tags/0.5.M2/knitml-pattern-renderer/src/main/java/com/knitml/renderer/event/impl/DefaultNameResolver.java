package com.knitml.renderer.event.impl;

import com.knitml.renderer.event.NameResolver;
import com.knitml.renderer.event.RenderingEvent;


public class DefaultNameResolver implements NameResolver {

	private static final String DEFAULT_PACKAGE_NAME = "com.knitml.renderer.visitor.model";
	private String packageName = DEFAULT_PACKAGE_NAME;
	
	public DefaultNameResolver() {
	}
	
	public DefaultNameResolver(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @see com.knitml.renderer.visitor.INameResolver#findTargetClassFromClassName(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Class<RenderingEvent> findTargetClassFromClassName(Object object) throws ClassNotFoundException {
		return (Class<RenderingEvent>)Class.forName(packageName + "." + object.getClass().getSimpleName() + "Visitor");
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
