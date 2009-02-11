package com.knitml.tools.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.knitml.renderer.RendererProgram;
import com.knitml.renderer.context.RenderingContextFactory;
import com.knitml.renderer.context.impl.SpringRenderingContextFactory;
import com.knitml.renderer.visitor.impl.DefaultVisitorFactory;
import com.knitml.validation.ValidationProgram;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;
import com.knitml.validation.visitor.instruction.impl.SpringVisitorFactory;

public abstract class RunnerTests {

	//protected static final String APP_CTX_VALIDATION = "applicationContext-validation.xml";
	protected static final String APP_CTX_RENDERER = "applicationContext-patternRenderer.xml";

	protected static ValidationProgram validator;
	protected static RendererProgram renderer;
	protected static KnittingContextFactory knittingContextFactory;
	protected static RenderingContextFactory renderingContextFactory;

	@BeforeClass
	public static void configureContextFactories() {
		//knittingContextFactory = new SpringKnittingContextFactory(APP_CTX_VALIDATION);
		knittingContextFactory = new DefaultKnittingContextFactory();
		renderingContextFactory = new SpringRenderingContextFactory(APP_CTX_RENDERER);
		validator = new ValidationProgram(knittingContextFactory, new SpringVisitorFactory());
		renderer = new RendererProgram(renderingContextFactory, new DefaultVisitorFactory());
	}

	@AfterClass
	public static void shutdownContextFactories() {
		knittingContextFactory.shutdown();
		renderingContextFactory.shutdown();
		validator = null;
		renderer = null;
	}

}
