package com.knitml.tools.runner;

import java.io.InputStream;
import java.util.Properties;

import org.junit.BeforeClass;
import org.springframework.core.io.DefaultResourceLoader;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.knitml.renderer.config.Configuration;
import com.knitml.renderer.config.DefaultModule;
import com.knitml.renderer.context.Options;
import com.knitml.renderer.program.RendererProgram;
import com.knitml.tools.runner.support.ConfigurationBuilder;

public abstract class RunnerTests {

	protected static final String APP_CTX_RENDERER = "conf/renderer.properties";

	protected static RendererProgram renderer;
	@BeforeClass
	public static void configureContextFactories() throws Exception {
		InputStream is = new DefaultResourceLoader().getResource(APP_CTX_RENDERER).getInputStream();
		Properties props = new Properties();
		props.load(is);
		is.close();
		Configuration configuration = new ConfigurationBuilder().buildConfiguration(props);
		final Options options = configuration.getOptions();
		Module optionsModule = new AbstractModule() {
			protected void configure() {
				bind(Options.class).toInstance(options);
			}
		};
		Injector injector = Guice.createInjector(optionsModule, configuration.getModule(), new DefaultModule());
		renderer = injector.getInstance(RendererProgram.class);
	}

}
