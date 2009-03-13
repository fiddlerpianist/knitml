package com.knitml.renderer.event.impl;

import com.knitml.renderer.event.EventHandler;
import com.knitml.renderer.event.NameResolver;


public class DefaultNameResolver implements NameResolver {

	private static final String DEFAULT_PACKAGE_NAME = "com.knitml.renderer.visitor.model";
	private String packageName = DEFAULT_PACKAGE_NAME;
	
	public DefaultNameResolver() {
	}
	
	public DefaultNameResolver(String packageName) {
		this.packageName = packageName;
	}

	@SuppressWarnings("unchecked")
	public Class<EventHandler> findTargetClassFromClassName(Object object) throws ClassNotFoundException {
		return (Class<EventHandler>)Class.forName(packageName + "." + object.getClass().getSimpleName() + "Handler");
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
