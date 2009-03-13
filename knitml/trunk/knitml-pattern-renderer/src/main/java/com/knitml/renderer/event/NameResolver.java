package com.knitml.renderer.event;

public interface NameResolver {

	Class<EventHandler> findTargetClassFromClassName(Object object)
			throws ClassNotFoundException;

}