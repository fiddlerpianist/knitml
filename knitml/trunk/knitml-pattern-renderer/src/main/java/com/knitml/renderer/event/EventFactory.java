package com.knitml.renderer.event;


public interface EventFactory {

	RenderingEvent findVisitorFromClassName(Object instance);
	void pushNameResolver(NameResolver nameResolver);
	NameResolver popNameResolver();

}
