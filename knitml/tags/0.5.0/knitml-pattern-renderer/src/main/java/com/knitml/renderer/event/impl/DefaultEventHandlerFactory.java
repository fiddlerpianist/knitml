package com.knitml.renderer.event.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.Stack;
import com.knitml.renderer.event.EventHandler;
import com.knitml.renderer.event.EventHandlerFactory;
import com.knitml.renderer.event.NameResolver;

public class DefaultEventHandlerFactory implements EventHandlerFactory {

	private final static Logger log = LoggerFactory
			.getLogger(DefaultEventHandlerFactory.class);

	protected Stack<NameResolver> nameResolvers = new Stack<NameResolver>();
	
	public DefaultEventHandlerFactory() {
		nameResolvers.push(new DefaultNameResolver());
	}

	public NameResolver popNameResolver() {
		return nameResolvers.pop();
	}

	public void pushNameResolver(NameResolver nameResolver) {
		nameResolvers.push(nameResolver);
	}

	public EventHandler findEventHandlerFromClassName(Object object) {
		try {
			Class<EventHandler> visitorClass = nameResolvers.peek().findTargetClassFromClassName(object);
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
