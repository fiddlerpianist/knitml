package com.knitml.renderer.event.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.Stack;
import com.knitml.renderer.event.EventFactory;
import com.knitml.renderer.event.NameResolver;
import com.knitml.renderer.event.RenderingEvent;

public class DefaultEventFactory implements EventFactory {

	private final static Logger log = LoggerFactory
			.getLogger(DefaultEventFactory.class);

	protected Stack<NameResolver> nameResolvers = new Stack<NameResolver>();
	
	public DefaultEventFactory() {
		nameResolvers.push(new DefaultNameResolver());
	}

	public NameResolver popNameResolver() {
		return nameResolvers.pop();
	}

	public void pushNameResolver(NameResolver nameResolver) {
		nameResolvers.push(nameResolver);
	}

	public RenderingEvent findVisitorFromClassName(Object object) {
		try {
			Class<RenderingEvent> visitorClass = nameResolvers.peek().findTargetClassFromClassName(object);
			RenderingEvent visitor = visitorClass.newInstance();
			if (visitor instanceof AbstractRenderingEvent) {
				((AbstractRenderingEvent)visitor).setVisitorFactory(this);
			}
			return visitor;
		} catch (Exception ex) {
			log.info("Could not find visitor class for [" + object + "]");
			return new ExceptionThrowingVisitor();
		}
	}

}
