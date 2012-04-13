package com.knitml.tools.runner.support;

import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.config.Configuration;
import com.knitml.renderer.context.Options;

public class SpringConfigurationBuilder {

	public Configuration getConfiguration(String configFile) {
		return getConfiguration(new String[] { configFile });
	}

	public Configuration getConfiguration(String[] configFiles) {
		ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				configFiles);
		try {
			Map<String, RendererFactory> rendererFactories = applicationContext
					.getBeansOfType(RendererFactory.class);
			if (rendererFactories.size() != 1) {
				throw new BeanDefinitionValidationException(
						"There must be exactly one bean of type "
								+ RendererFactory.class.getName()
								+ " in the application context");
			}
			final RendererFactory rendererFactory = rendererFactories.values().iterator()
					.next();
			Map<String, Options> optionsMap = applicationContext
					.getBeansOfType(Options.class);
			if (optionsMap.size() != 1) {
				throw new BeanDefinitionValidationException(
						"There must be exactly one bean of type "
								+ Options.class.getName()
								+ " in the application context");
			}
			final Options options = optionsMap.values().iterator().next();
			Module module = new AbstractModule() {
				protected void configure() {
					bind(RendererFactory.class).toInstance(rendererFactory);
				}
			};
			return new Configuration(module, options);
		} finally {
			applicationContext.close();
		}
	}
}
