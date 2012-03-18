/**
 * 
 */
package test.support;

import static test.support.JiBXUtils.parseXml;

import java.io.StringWriter;

import org.jibx.runtime.JiBXException;
import org.junit.Before;
import org.junit.runner.RunWith;

import test.support.GuiceJUnit4Runner.GuiceModules;

import com.google.inject.Inject;
import com.knitml.core.model.pattern.Pattern;
import com.knitml.core.model.pattern.Version;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.config.DefaultModule;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.listener.RenderingPatternEventListenerFactory;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.PatternEventListener;
import com.knitml.validation.visitor.instruction.Visitor;
import com.knitml.validation.visitor.instruction.VisitorFactory;

@RunWith(GuiceJUnit4Runner.class)
@GuiceModules( {BasicRendererTestModule.class } )
public abstract class AbstractRenderingContextTests {

	// initialized from Spring context
	// protected RendererFactory rendererFactory;
	// protected Options options;

	// created in setItUp()
	// protected RenderingContext renderingContext;
	// protected Renderer renderer;
	// place to capture results from renderer
	private StringWriter outputCapturer;

	protected static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	public static final String PATTERN_START_TAG = "<pattern:pattern xmlns:pattern=\"http://www.knitml.com/schema/pattern\" xmlns:common=\"http://www.knitml.com/schema/common\" xmlns=\"http://www.knitml.com/schema/operations\" version=\"" + Version.getCurrentVersionId() + "\">"; //$NON-NLS-1$ //$NON-NLS-2$

	// injected variables
	@Inject
	protected RendererFactory rendererFactory;
	@Inject
	protected RenderingPatternEventListenerFactory eventListenerFactory;
	@Inject
	protected RenderingContext renderingContext;
	@Inject
	protected VisitorFactory visitorFactory;

	// retrieved / derived from injected fields
	protected KnittingContext knittingContext;
	protected PatternEventListener listener;
	protected Renderer renderer;

	@Before
	public void initialize() {
		this.outputCapturer = new StringWriter(128);
		this.renderer = rendererFactory
				.create(renderingContext, outputCapturer);
		this.listener = eventListenerFactory.create(this.renderer);
		this.knittingContext = renderingContext.getKnittingContext();
		this.knittingContext.getListenerManager().addListener(listener);
	}

	protected static final String DEFAULT_BINDING_NAME = "pattern-binding";

	protected StringWriter getOutputCapturer() {
		return outputCapturer;
	}

	protected String getOutput() {
		return getOutputCapturer().toString();
	}

	protected void visitDocument(Object object) throws RenderingException,
			KnittingEngineException {
		Visitor visitor = visitorFactory.findVisitorFromClassName(object);
		listener.begin(object, knittingContext);
		visitor.visit(object, knittingContext);
		listener.end(object, knittingContext);
	}

	protected Pattern processXml(String xml) throws JiBXException,
			RenderingException, KnittingEngineException {
		return processXml(xml, Pattern.class);
	}

	protected <C> C processXml(String xml, Class<C> rootClass)
			throws JiBXException, RenderingException, KnittingEngineException {
		C object = parseXml(xml, rootClass);
		visitDocument(object);
		return object;
	}

}
