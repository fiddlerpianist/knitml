package com.knitml.renderer.util;

import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.context.Options;

public class SpringConfigurationBuilder {

	public Configuration getConfiguration(String configFile) {
		return getConfiguration(new String[] { configFile });
	}

	@SuppressWarnings("unchecked")
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
			Map<String, Options> options = applicationContext
					.getBeansOfType(Options.class);
			if (options.size() != 1) {
				throw new BeanDefinitionValidationException(
						"There must be exactly one bean of type "
								+ Options.class.getName()
								+ " in the application context");
			}
			return new Configuration(rendererFactories.values().iterator()
					.next(), options.values().iterator().next());
		} finally {
			applicationContext.close();
		}
	}
}
