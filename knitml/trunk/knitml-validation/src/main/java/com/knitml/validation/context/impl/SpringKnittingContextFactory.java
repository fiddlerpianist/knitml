package com.knitml.validation.context.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.knitml.engine.KnittingEngine;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.KnittingContextFactory;

/**
 * The Spring implementation of a KnittingContextFactory. It looks for beans
 * named <code>knittingContext</code>, <code>knittingEngine</code>, and
 * <code>knittingFactory</code>. It also expects that the knitting context is
 * of type {@link Recorder}.
 * 
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class SpringKnittingContextFactory implements KnittingContextFactory {

	protected ApplicationContext beanFactory;
	private boolean shutdownBeanFactory = true;

	public SpringKnittingContextFactory(ApplicationContext applicationContext) {
		this.beanFactory = applicationContext;
		// since we didn't create it, don't shut it down
		this.shutdownBeanFactory = false;
	}

	public SpringKnittingContextFactory(String configFile) {
		this.beanFactory = new ClassPathXmlApplicationContext(configFile);
	}

	public SpringKnittingContextFactory(String[] configFiles) {
		this.beanFactory = new ClassPathXmlApplicationContext(configFiles);
	}

	public KnittingContext createKnittingContext() {
		KnittingContext knittingContext = (KnittingContext) beanFactory
				.getBean("knittingContext");
		KnittingEngine engine = (KnittingEngine) beanFactory
				.getBean("knittingEngine");
		knittingContext.setEngine(engine);
		return knittingContext;
	}

	public void shutdown() {
		if (shutdownBeanFactory && beanFactory instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext)beanFactory).close();
		}
	}

}
