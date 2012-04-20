package com.knitml.renderer.program;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.commons.io.output.NullWriter;
import org.jibx.runtime.JiBXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.knitml.core.model.pattern.Parameters;
import com.knitml.core.model.pattern.Pattern;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.RendererFactory;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.InstructionOption;
import com.knitml.renderer.context.Options;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.listener.RenderingPatternEventListenerFactory;
import com.knitml.validation.ValidationProgram;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.PatternEventListener;
import com.knitml.validation.visitor.instruction.VisitorFactory;

public class RendererProgram {

	final static Logger log = LoggerFactory.getLogger(RendererProgram.class);

	// default if not passed
	private RendererFactory rendererFactory;
	private Provider<List<PatternEventListener>> eventListenerCollectionProvider;
	private RenderingPatternEventListenerFactory renderingListenerFactory;
	private KnittingContextFactory knittingContextFactory;
	private VisitorFactory visitorFactory;
	private Options options;

	@Inject
	public RendererProgram(RendererFactory rendererFactory,
			Provider<List<PatternEventListener>> eventListenerSetProvider,
			RenderingPatternEventListenerFactory eventListenerFactory,
			KnittingContextFactory knittingContextFactory,
			VisitorFactory visitorFactory, Options options) {
		this.rendererFactory = rendererFactory;
		this.renderingListenerFactory = eventListenerFactory;
		this.eventListenerCollectionProvider = eventListenerSetProvider;
		this.knittingContextFactory = knittingContextFactory;
		this.visitorFactory = visitorFactory;
		this.options = options;
	}

	public Pattern render(Parameters parameters) throws SAXException,
			JiBXException, IOException, RenderingException,
			KnittingEngineException {
		return render(parameters, new HashSet<InstructionOption>());
	}

	public Pattern render(Parameters parameters,
			Set<InstructionOption> instructionOptions) throws SAXException,
			JiBXException, IOException, RenderingException,
			KnittingEngineException {

		long startTime = System.currentTimeMillis();
		log.info("Begin rendering pattern ID {}", startTime);
		// TODO need to handle instruction options

		try {
			// create the rendering context based on user's options
			RenderingContext renderingContext = new RenderingContext(options);
			// set the writer on the listener adapter (not the one sent to the
			// processing program as parameters)
			Writer writer = parameters.getWriter() == null ? new NullWriter() : parameters.getWriter();
			parameters.setWriter(null);
			List<PatternEventListener> eventListeners = eventListenerCollectionProvider
					.get();

			// create the renderer using the rendering context
			Renderer renderer = rendererFactory
					.create(renderingContext, writer);
			// create a listener which listens to model events and passes them
			// to
			// the renderer
			PatternEventListener renderingListener = this.renderingListenerFactory
					.create(renderer);
			eventListeners.add(renderingListener);

			// set up the validation program by adding the rendering listener to
			// it
			// List<PatternEventListener> listeners = new
			// ArrayList<PatternEventListener>();
			// listeners.add(listener);
			ValidationProgram processor = new ValidationProgram(
					knittingContextFactory, visitorFactory, eventListeners);

			// run the validation with rendering attached as an event listener.
			// The object returned contains any changes made by the validator
			// and listeners
			return processor.validate(parameters);
		} finally {
			long finishedTime = System.currentTimeMillis();
			log.info("Finished rendering pattern ID {}; took {} ms", startTime,	finishedTime - startTime);
		}
	}
}
