/**
 * 
 */
package test.support;

import java.io.StringReader;
import java.io.StringWriter;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.After;
import org.junit.Before;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.knitml.core.model.Pattern;
import com.knitml.core.model.Version;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.Options;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.listener.RenderingListenerAdapter;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.ListenerManager;
import com.knitml.validation.context.PatternEventListener;
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;
import com.knitml.validation.context.impl.DefaultListenerManager;
import com.knitml.validation.visitor.instruction.Visitor;
import com.knitml.validation.visitor.instruction.VisitorFactory;
import com.knitml.validation.visitor.instruction.impl.DefaultVisitorFactory;

@RunWith(JUnit4ClassRunner.class)
public abstract class AbstractRenderingContextTests extends AbstractDependencyInjectionSpringContextTests {

	// initialized from Spring context
	protected RendererFactory rendererFactory;
	protected Options options;
	
	// created in setItUp()
	protected RenderingContext renderingContext;
	protected Renderer renderer;
	// place to capture results from renderer
	private StringWriter outputCapturer;
	
	protected static final String LINE_SEPARATOR = System.getProperty("line.separator");
	protected static final String PATTERN_START_TAG = "<pattern xmlns=\"http://www.knitml.com/schema/pattern\" version=\"" + Version.getCurrentVersionId() + "\">";

	// knitting context variables
	protected PatternEventListener listener;
	protected KnittingContext knittingContext;
	protected VisitorFactory visitorFactory = new DefaultVisitorFactory(); // stateless, so init here
	
	protected StringWriter getOutputCapturer() {
		return outputCapturer;
	}

	protected String getOutput() {
		return getOutputCapturer().toString();
	}

	@Before
	public final void setItUp() throws Exception {
		// populates renderingContext
		setPopulateProtectedVariables(true);
		setDependencyCheck(false);
		this.outputCapturer = new StringWriter(128);
		setUp();
		// use renderingContext provided by Spring
		this.renderingContext = new RenderingContext(options);
		this.renderer = rendererFactory.createRenderer(renderingContext, outputCapturer);
		this.listener = new RenderingListenerAdapter(renderer);
		this.knittingContext = setUpKnittingContext(listener);
		this.renderingContext.setKnittingContext(knittingContext);
		onSetItUp();
	}
	
	protected KnittingContext setUpKnittingContext(PatternEventListener listener) throws Exception {
		// create the listener manager and add a listener to it
		// create the knitting context 
		KnittingContextFactory contextFactory = new DefaultKnittingContextFactory();
		KnittingContext context = contextFactory.createKnittingContext();
		// set the listener manager on the context
		ListenerManager listenerManager = new DefaultListenerManager();
		listenerManager.addListener(listener);
		context.setListenerManager(listenerManager);
		return context;
	}

	@After
	public void tearItDown() throws Exception {
		tearDown();
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] { "applicationContext-patternRenderer.xml" };
	}

	protected void onSetItUp() throws Exception {
	}
	
	@SuppressWarnings("unchecked")
	protected <C> C parseXml(String xml, Class<C> rootClass) throws JiBXException {
		IBindingFactory factory = BindingDirectory.getFactory(rootClass);
		IUnmarshallingContext uctx = factory.createUnmarshallingContext();
		return (C) uctx.unmarshalDocument(new StringReader(xml));
	}

	protected void visitDocument(Object object) throws RenderingException, KnittingEngineException {
		Visitor visitor = visitorFactory.findVisitorFromClassName(object);
		listener.begin(object, knittingContext);
		visitor.visit(object, knittingContext);
		listener.end(object, knittingContext);
	}

	protected Pattern processXml(String xml) throws JiBXException, RenderingException, KnittingEngineException {
		return processXml(xml, Pattern.class);
	}
	
	protected <C> C processXml(String xml, Class<C> rootClass) throws JiBXException, RenderingException, KnittingEngineException {
		C object = parseXml(xml, rootClass);
		visitDocument(object);
		return object;
	}

}
