package com.knitml.validation.visitor.instruction.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.compatibility.Stack;
import com.knitml.validation.visitor.instruction.NameResolver;
import com.knitml.validation.visitor.instruction.Visitor;
import com.knitml.validation.visitor.instruction.VisitorFactory;

/**
 * A simple visitor factory that does not provide advice. This keeps the stack
 * trace much smaller for times when there is a lot of step debugging.
 */
public class SimpleVisitorFactory implements VisitorFactory {

	private final static Logger log = LoggerFactory
			.getLogger(SimpleVisitorFactory.class);

	protected Stack<NameResolver> nameResolvers = new Stack<NameResolver>();

	public SimpleVisitorFactory() {
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
			log.error("Could not find visitor class for {}", object); //$NON-NLS-1$
			return new ExceptionThrowingVisitor(ex);
		}
	}

}
