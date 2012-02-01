package com.knitml.renderer.event;


public interface EventHandlerFactory {

	EventHandler findEventHandlerFromClassName(Object instance);
	void pushNameResolver(NameResolver nameResolver);
	NameResolver popNameResolver();

}
