package com.knitml.validation.visitor.instruction.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.Stack;
import com.knitml.validation.visitor.instruction.NameResolver;
import com.knitml.validation.visitor.instruction.Visitor;
import com.knitml.validation.visitor.instruction.VisitorFactory;

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

	public Visitor findVisitorFromClassName(Object object) {
		try {
			Class<Visitor> visitorClass = nameResolvers.peek()
					.findVisitingClassFromClassName(object);
			Visitor visitor = visitorClass.newInstance();
			if (visitor instanceof AbstractPatternVisitor) {
				((AbstractPatternVisitor) visitor).setVisitorFactory(this);
			}
			return visitor;
		} catch (Exception ex) {
			log.info("Could not find visitor class for [" + object + "]");
			return new ExceptionThrowingVisitor();
		}
	}

}
