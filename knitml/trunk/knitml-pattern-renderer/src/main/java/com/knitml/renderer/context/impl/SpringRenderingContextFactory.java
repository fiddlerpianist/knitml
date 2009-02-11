package com.knitml.renderer.context.impl;

import java.io.Writer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.context.RenderingContextFactory;

public class SpringRenderingContextFactory implements RenderingContextFactory {

	protected ApplicationContext beanFactory;
	private boolean shutdownBeanFactory = true;

	public SpringRenderingContextFactory(ApplicationContext ctx) {
		this.beanFactory = ctx;
	}
	
	public SpringRenderingContextFactory(String configFile) {
		this.beanFactory = new ClassPathXmlApplicationContext(configFile);
	}

	public SpringRenderingContextFactory(String[] configFiles) {
		this.beanFactory = new ClassPathXmlApplicationContext(configFiles);
	}

	public RenderingContext createRenderingContext() {
		RenderingContext context = (RenderingContext) this.beanFactory.getBean("renderingContext");
		context.getRenderer().setRenderingContext(context);
		return context;
	}
	
	public RenderingContext createRenderingContext(Writer writer) {
		RenderingContext context = createRenderingContext();
		context.getRenderer().setWriter(writer);
		return context;
	}
	public void shutdown() {
		if (shutdownBeanFactory && beanFactory instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext)beanFactory).close();
		}
	}
}
