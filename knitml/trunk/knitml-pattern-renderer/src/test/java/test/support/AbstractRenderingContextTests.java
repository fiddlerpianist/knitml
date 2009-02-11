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
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.Renderer;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.RenderingVisitor;
import com.knitml.renderer.visitor.VisitorFactory;
import com.knitml.renderer.visitor.impl.DefaultVisitorFactory;

@RunWith(JUnit4ClassRunner.class)
public abstract class AbstractRenderingContextTests extends AbstractDependencyInjectionSpringContextTests {

	protected VisitorFactory visitorFactory;
	protected Renderer renderer;
	protected RenderingContext renderingContext;
	private StringWriter outputCapturer;
	protected static final String LINE_SEPARATOR = System.getProperty("line.separator");

	protected StringWriter getOutputCapturer() {
		return outputCapturer;
	}

	protected String getOutput() {
		return getOutputCapturer().toString();
	}

	@Before
	public final void setItUp() throws Exception {
		setPopulateProtectedVariables(true);
		setDependencyCheck(false);
		this.outputCapturer = new StringWriter(128);
		setUp();
		this.visitorFactory = new DefaultVisitorFactory();
		this.renderer = renderingContext.getRenderer();
		renderer.setWriter(this.outputCapturer);
		renderer.setRenderingContext(renderingContext);
		onSetItUp();
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

	protected void visitDocument(Object object) throws RenderingException {
		RenderingVisitor visitor = this.visitorFactory.findVisitorFromClassName(object);
		visitor.visit(object, this.renderingContext);
	}

	protected Pattern processXml(String xml) throws JiBXException, RenderingException {
		return processXml(xml, Pattern.class);
	}
	
	protected <C> C processXml(String xml, Class<C> rootClass) throws JiBXException, RenderingException {
		C object = parseXml(xml, rootClass);
		visitDocument(object);
		return object;
	}

}
