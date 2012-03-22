package com.knitml.renderer.event.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.renderer.event.EventHandler;
import com.knitml.renderer.event.EventHandlerFactory;
import com.knitml.renderer.nameresolver.NameResolver;

public class DefaultEventHandlerFactory implements EventHandlerFactory {

	private final static Logger log = LoggerFactory
			.getLogger(DefaultEventHandlerFactory.class);

	public EventHandler findEventHandlerFromClassName(Object object, NameResolver nameResolver) {
		try {
			Class<EventHandler> visitorClass = nameResolver.findTargetClassFromClassName(object);
			EventHandler visitor = visitorClass.newInstance();
			if (visitor instanceof AbstractEventHandler) {
				((AbstractEventHandler)visitor).setEventHandlerFactory(this);
			}
			return visitor;
		} catch (Exception ex) {
			log.info("Could not find event handler class for [" + object + "]");
			return new ExceptionThrowingHandler();
		}
	}

}
