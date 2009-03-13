package com.knitml.tools.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.program.RendererProgram;
import com.knitml.renderer.util.Configuration;
import com.knitml.renderer.util.SpringConfigurationBuilder;
import com.knitml.validation.ValidationProgram;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;
import com.knitml.validation.visitor.instruction.VisitorFactory;
import com.knitml.validation.visitor.instruction.impl.SpringVisitorFactory;

public abstract class RunnerTests {

	protected static final String APP_CTX_RENDERER = "applicationContext-patternRenderer.xml";

	protected static ValidationProgram validator;
	protected static RendererProgram renderer;
	protected static KnittingContextFactory knittingContextFactory;
	protected static VisitorFactory knittingVisitorFactory;
	protected static RendererFactory rendererFactory;

	@BeforeClass
	public static void configureContextFactories() {
		knittingContextFactory = new DefaultKnittingContextFactory();
		knittingVisitorFactory = new SpringVisitorFactory();
		Configuration configuration = new SpringConfigurationBuilder().getConfiguration(APP_CTX_RENDERER); 
		rendererFactory = configuration.getRendererFactory();
		validator = new ValidationProgram(knittingContextFactory,
				knittingVisitorFactory);
		renderer = new RendererProgram(rendererFactory);
		renderer.setOptions(configuration.getOptions());
	}

	@AfterClass
	public static void shutdownContextFactories() {
		knittingContextFactory.shutdown();
	}

}
