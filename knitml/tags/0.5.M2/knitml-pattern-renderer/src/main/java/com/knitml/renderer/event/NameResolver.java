package com.knitml.renderer.event;

public interface NameResolver {

	Class<RenderingEvent> findTargetClassFromClassName(Object object)
			throws ClassNotFoundException;

}