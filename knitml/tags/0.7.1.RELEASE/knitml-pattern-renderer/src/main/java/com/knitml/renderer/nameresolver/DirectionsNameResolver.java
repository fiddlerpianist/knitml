package com.knitml.renderer.nameresolver;

import com.knitml.renderer.event.EventHandler;


public class DirectionsNameResolver implements NameResolver {

	private String packageName = "com.knitml.renderer.handler.model";
	
	@SuppressWarnings("unchecked")
	public Class<EventHandler> findTargetClassFromClassName(Object object) throws ClassNotFoundException {
		return (Class<EventHandler>)Class.forName(packageName + "." + object.getClass().getSimpleName() + "Handler");
	}
	
}
