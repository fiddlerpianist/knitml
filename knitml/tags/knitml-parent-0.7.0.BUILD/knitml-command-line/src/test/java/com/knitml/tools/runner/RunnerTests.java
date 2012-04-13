package com.knitml.tools.runner;

import org.junit.BeforeClass;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.knitml.renderer.config.Configuration;
import com.knitml.renderer.config.DefaultModule;
import com.knitml.renderer.context.Options;
import com.knitml.renderer.program.RendererProgram;
import com.knitml.tools.runner.support.SpringConfigurationBuilder;

public abstract class RunnerTests {

	protected static final String APP_CTX_RENDERER = "applicationContext-patternRenderer.xml";

	protected static RendererProgram renderer;
	@BeforeClass
	public static void configureContextFactories() {
		Configuration configuration = new SpringConfigurationBuilder().getConfiguration(APP_CTX_RENDERER);
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
