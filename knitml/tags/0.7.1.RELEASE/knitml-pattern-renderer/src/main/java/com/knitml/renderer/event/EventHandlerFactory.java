package com.knitml.renderer.event;

import com.knitml.renderer.nameresolver.NameResolver;


public interface EventHandlerFactory {

	EventHandler findEventHandlerFromClassName(Object instance, NameResolver nameResolver);

}
