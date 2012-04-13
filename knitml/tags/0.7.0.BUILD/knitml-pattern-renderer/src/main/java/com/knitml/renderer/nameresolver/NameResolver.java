package com.knitml.renderer.nameresolver;

import com.knitml.renderer.event.EventHandler;

public interface NameResolver {

	Class<EventHandler> findTargetClassFromClassName(Object object)
			throws ClassNotFoundException;

}