package com.knitml.validation.visitor.instruction.impl;

import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;

import com.knitml.core.compatibility.Stack;
import com.knitml.validation.visitor.instruction.NameResolver;
import com.knitml.validation.visitor.instruction.Visitor;
import com.knitml.validation.visitor.instruction.VisitorFactory;

public class SpringVisitorFactory implements VisitorFactory {

	private final static Logger log = LoggerFactory
			.getLogger(SpringVisitorFactory.class);

	protected Stack<NameResolver> nameResolvers = new Stack<NameResolver>();
	private Advice advice = new ElementDescriptionAfterThrowingAdvice(); 

	public SpringVisitorFactory() {
		nameResolvers.push(new DefaultNameResolver());
	}

	public Visitor findVisitorFromClassName(Object instance) {
		try {
			Class<Visitor> visitorClass = nameResolvers.peek()
					.findVisitingClassFromClassName(instance);
			Object target = visitorClass.newInstance();
			if (target instanceof AbstractPatternVisitor) {
				((AbstractPatternVisitor) target).setVisitorFactory(this);
			}
			ProxyFactory proxyFactory = new ProxyFactory();
			proxyFactory.addInterface(Visitor.class);
			proxyFactory.addAdvice(advice);
			proxyFactory.setTarget(target);
			return (Visitor) proxyFactory.getProxy(this.getClass().getClassLoader());
		} catch (Exception ex) {
			log.info("Could not find visitor class for element named [{}]",instance.getClass());
			return new ExceptionThrowingVisitor(ex);
		}
	}

	public NameResolver popNameResolver() {
		return nameResolvers.pop();
	}

	public void pushNameResolver(NameResolver nameResolver) {
		nameResolvers.push(nameResolver);
	}

}
