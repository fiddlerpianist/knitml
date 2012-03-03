package com.knitml.validation;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.IOUtils;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.knitml.core.model.pattern.Parameters;
import com.knitml.core.model.pattern.Pattern;
import com.knitml.core.xml.EntityResolverWrapper;
import com.knitml.core.xml.PluggableSchemaResolver;
import com.knitml.core.xml.Schemas;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.PatternEventListener;
import com.knitml.validation.context.ListenerManager;
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;
import com.knitml.validation.context.impl.DefaultListenerManager;
import com.knitml.validation.visitor.instruction.Visitor;
import com.knitml.validation.visitor.instruction.VisitorFactory;
import com.knitml.validation.visitor.instruction.impl.DefaultVisitorFactory;
import com.knitml.validation.visitor.instruction.impl.SpringVisitorFactory;

public class ValidationProgram {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(ValidationProgram.class);

	private KnittingContextFactory contextFactory = new DefaultKnittingContextFactory();
	private VisitorFactory visitorFactory = new DefaultVisitorFactory();
	private ListenerManager listenerManager = new DefaultListenerManager();

	/**
	 * Constructs the program with the specified listener, using defaults for its various factories.
	 * 
	 * @param listener
	 */
	public ValidationProgram(PatternEventListener listener) {
		this(listener, false);
	}

	/**
	 * Constructs the program with the specified listener, using defaults for its various factories.
	 * 
	 * @param listener
	 * @param traceability whether line numbers should be reported when 
	 */
	public ValidationProgram(PatternEventListener listener, boolean traceability) {
		if (traceability) {
			// supports line numbers for error reporting
			visitorFactory = new SpringVisitorFactory();
		}
		listenerManager.addListener(listener);
	}
	
	/**
	 * Constructs the program with the specified listener, using defaults for its various factories.
	 * 
	 * @param listener
	 * @param traceability whether line numbers should be reported when 
	 */
	public ValidationProgram(boolean traceability) {
		this(null, traceability);
	}
	
	/**
	 * Constructs the program with the specified KnittingContextFactory and VisitorFactory
	 * (with no listeners).
	 * 
	 * @param ctxFactory
	 * @param visitorFactory
	 */
	public ValidationProgram(KnittingContextFactory ctxFactory,
			VisitorFactory visitorFactory) {
		this.contextFactory = ctxFactory;
		this.visitorFactory = visitorFactory;
	}

	/**
	 * Constructs the program with the specified KnittingContextFactory, VisitorFactory and listeners
	 * attached to the processing model. This constructor provides maximum flexibility for clients.
	 * 
	 * @param ctxFactory
	 * @param visitorFactory
	 * @param listeners
	 */
	public ValidationProgram(KnittingContextFactory ctxFactory,
			VisitorFactory visitorFactory, List<PatternEventListener> listeners) {
		this(ctxFactory, visitorFactory);
		for (PatternEventListener listener : listeners) {
			listenerManager.addListener(listener);
		}
	}

	public Pattern validate(Parameters parameters) throws SAXException,
			JiBXException, IOException, KnittingEngineException {
		KnittingContext context = contextFactory.createKnittingContext();
		context.setListenerManager(listenerManager);

		// Document document = parameters.getDocument();
		Pattern pattern = parameters.getPattern();
		Reader reader = parameters.getReader();
		if (pattern == null && reader == null) {
			throw new IllegalArgumentException(
					"One of pattern or reader must be specified in the Parameters object"); //$NON-NLS-1$
		}

		if (pattern == null) {
			// we are not passing in the object model, so...

			// 1) validate with JAXP
			if (parameters.isCheckSyntax()) {
				// we have to read from the stream first and store the string in
				// memory, so that we can read twice. (ugh)
				// not sure how else to do this, short of creating a temp file
				StringWriter writer = new StringWriter();
				IOUtils.copy(reader, writer);
				reader.close();
				writer.close();

				Source source = new StreamSource(new StringReader(writer
						.toString()));
				checkSyntax(source);
				// now use a second reader for later
				reader = new StringReader(writer.toString());
			}
			// 2) Use JiBX to transform to the object model
			IBindingFactory factory = BindingDirectory
					.getFactory(Pattern.class);
			IUnmarshallingContext uctx = factory.createUnmarshallingContext();
			pattern = (Pattern) uctx.unmarshalDocument(reader);
		}

		Visitor visitor = visitorFactory.findVisitorFromClassName(pattern);
		visitor.visit(pattern, context);
		// the document has now been validated
		Writer writer = parameters.getWriter();
		if (writer != null) {
			try {
				IBindingFactory factory = BindingDirectory
						.getFactory(Pattern.class);
				IMarshallingContext mctx = factory.createMarshallingContext();
				mctx.setOutput(writer);
				mctx.getXmlWriter().setIndentSpaces(2, null, ' ');
				mctx.getXmlWriter().writeXMLDecl("1.0", "UTF-8", null); //$NON-NLS-1$ //$NON-NLS-2$
				mctx.marshalDocument(pattern);
			} finally {
				if (writer != null) {
					writer.close();
				}
			}
		}
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
