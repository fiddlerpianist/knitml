package com.knitml.renderer;

import java.io.IOException;
import java.io.Reader;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.knitml.core.common.Parameters;
import com.knitml.core.model.Pattern;
import com.knitml.core.xml.EntityResolverWrapper;
import com.knitml.core.xml.PluggableSchemaResolver;
import com.knitml.core.xml.Schemas;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.context.RenderingContextFactory;
import com.knitml.renderer.visitor.RenderingVisitor;
import com.knitml.renderer.visitor.VisitorFactory;

public class RendererProgram {

	private final static Logger log = LoggerFactory
			.getLogger(RendererProgram.class);

	private RenderingContextFactory contextFactory;
	private VisitorFactory visitorFactory;

	public RendererProgram(RenderingContextFactory ctxFactory,
			VisitorFactory visitorFactory) {
		this.contextFactory = ctxFactory;
		this.visitorFactory = visitorFactory;
	}

	public Pattern render(Parameters parameters) throws SAXException,
			JiBXException, IOException, RenderingException {
		Pattern pattern = parameters.getPattern();
		Reader reader = parameters.getReader();
		if (pattern == null && reader == null) {
			throw new IllegalArgumentException(
					"One of pattern or reader must be specified "
							+ "in the Parameters object");
		}

		RenderingContext context = null;
		if (parameters.getWriter() == null) {
			log
					.warn("No writer was provided; will use the default writer for the Renderer (which may do nothing)");
			context = contextFactory.createRenderingContext();
		} else {
			context = contextFactory.createRenderingContext(parameters
					.getWriter());
		}

		if (pattern == null) {
			// we are not passing in the object model, so...

			// 1) validate with JAXP
			if (parameters.isCheckSyntax()) {
				Source source = new StreamSource(reader);
				checkSyntax(source);
			}
			// 2) Use JiBX to transform to the object model
			IBindingFactory factory = BindingDirectory
					.getFactory(Pattern.class);
			IUnmarshallingContext uctx = factory.createUnmarshallingContext();
			pattern = (Pattern) uctx.unmarshalDocument(reader);
		}
		RenderingVisitor visitor = visitorFactory.findVisitorFromClassName(pattern);
		visitor.visit(pattern, context);
		return pattern;
	}

	protected void checkSyntax(Source source) throws IOException, SAXException {

		EntityResolver entityResolver = new PluggableSchemaResolver(this
				.getClass().getClassLoader());
		LSResourceResolver resourceResolver = new EntityResolverWrapper(
				entityResolver);

		// create a SchemaFactory capable of understanding XML Schema
		SchemaFactory factory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		// set a strategy to validate included XML schemas
		factory.setResourceResolver(resourceResolver);

		// set primary XML to validate
		InputSource patternSchema = entityResolver.resolveEntity(null,
				Schemas.CURRENT_PATTERN_SCHEMA);
		Source patternSchemaSource = new StreamSource(patternSchema
				.getByteStream());
		Schema schema = factory.newSchema(patternSchemaSource);

		// create a Validator instance to validate the document
		Validator validator = schema.newValidator();

		// validate the tree
		validator.validate(source);
	}

}
