package com.knitml.renderer.impl.charting.analyzer;

import java.io.StringReader;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.junit.Before;

import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.context.RenderingContextFactory;
import com.knitml.renderer.context.impl.DefaultRenderingContextFactory;

public class ChartingAnalyzerTestsJava {

	protected RenderingContext context;
	protected ChartingAnalyzer analyzer;

	@Before
	public void setUp() {
		RenderingContextFactory factory = new DefaultRenderingContextFactory();
		context = factory.createRenderingContext();
		analyzer = new ChartingAnalyzer(context, false);
	}

	protected <C> C parseXml(String xml, Class<C> rootClass) throws Exception {
		IBindingFactory factory = BindingDirectory.getFactory(rootClass);
		IUnmarshallingContext uctx = factory.createUnmarshallingContext();
		return (C) uctx.unmarshalDocument(new StringReader(xml));
	}

}
