package com.knitml.renderer.visitor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.Stack;
import com.knitml.renderer.visitor.NameResolver;
import com.knitml.renderer.visitor.RenderingVisitor;
import com.knitml.renderer.visitor.VisitorFactory;

public class DefaultVisitorFactory implements VisitorFactory {

	private final static Logger log = LoggerFactory
			.getLogger(DefaultVisitorFactory.class);

	protected Stack<NameResolver> nameResolvers = new Stack<NameResolver>();
	
	public DefaultVisitorFactory() {
		nameResolvers.push(new DefaultNameResolver());
	}

	public NameResolver popNameResolver() {
		return nameResolvers.pop();
	}

	public void pushNameResolver(NameResolver nameResolver) {
		nameResolvers.push(nameResolver);
	}

	public RenderingVisitor findVisitorFromClassName(Object object) {
		try {
			Class<RenderingVisitor> visitorClass = nameResolvers.peek().findVisitingClassFromClassName(object);
			RenderingVisitor visitor = visitorClass.newInstance();
			if (visitor instanceof AbstractRenderingVisitor) {
				((AbstractRenderingVisitor)visitor).setVisitorFactory(this);
			}
			return visitor;
		} catch (Exception ex) {
			log.info("Could not find visitor class for [" + object + "]");
			return new ExceptionThrowingVisitor();
		}
	}

}
