package com.knitml.renderer.program;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.knitml.renderer.event.EventHandlerFactory;
import com.knitml.renderer.event.impl.DefaultEventHandlerFactory;
import com.knitml.renderer.impl.basic.BasicTextRendererFactory;
import com.knitml.renderer.listener.RenderingListenerAdapter;
import com.knitml.validation.ValidationProgram;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.PatternEventListener;
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;
import com.knitml.validation.visitor.instruction.VisitorFactory;
import com.knitml.validation.visitor.instruction.impl.SpringVisitorFactory;

// TODO this initialization is mind-numbingly complex because these classes were never meant to work together like this
public class RendererProgram {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(RendererProgram.class);

	// default if not passed
	private RendererFactory rendererFactory = new BasicTextRendererFactory();
	private EventHandlerFactory eventHandlerFactory = new DefaultEventHandlerFactory();

	public void setEventHandlerFactory(EventHandlerFactory eventHandlerFactory) {
		this.eventHandlerFactory = eventHandlerFactory;
	}

	public void setKnittingContextFactory(
			KnittingContextFactory knittingContextFactory) {
		this.knittingContextFactory = knittingContextFactory;
	}

	public void setVisitorFactory(VisitorFactory visitorFactory) {
		this.visitorFactory = visitorFactory;
	}

	private KnittingContextFactory knittingContextFactory = new DefaultKnittingContextFactory();
	private VisitorFactory visitorFactory = new SpringVisitorFactory();

	private Options options = new Options();

	public RendererProgram(RendererFactory rendererFactory) {
		this.rendererFactory = rendererFactory;
	}

	public void setOptions(Options options) {
		if (options != null) {
			this.options = options;
		}
	}

	public Pattern render(Parameters parameters) throws SAXException,
			JiBXException, IOException, RenderingException, KnittingEngineException {
		return render(parameters, new HashSet<InstructionOption>());
	}

	public Pattern render(Parameters parameters,
			Set<InstructionOption> instructionOptions) throws SAXException,
			JiBXException, IOException, RenderingException, KnittingEngineException {

		// TODO need to handle instruction options

		// create the rendering context based on user's options
		RenderingContext renderingContext = new RenderingContext(options);
		// set the writer on the listener adapter (not the one sent to the processing program as parameters)
		Writer writer = parameters.getWriter();
		parameters.setWriter(null);

		// create the renderer using the rendering context
		Renderer renderer = rendererFactory.createRenderer(renderingContext, writer);
		// create a listener which listens to model events and passes them to
		// the renderer
		RenderingListenerAdapter listener = new RenderingListenerAdapter(
				renderer);
		// set the factory used to create handlers for each event
		listener.setEventHandlerFactory(this.eventHandlerFactory);

		// set up the validation program by adding the rendering listener to it
		List<PatternEventListener> listeners = new ArrayList<PatternEventListener>();
		listeners.add(listener);
		ValidationProgram processor = new ValidationProgram(knittingContextFactory, visitorFactory, listeners);

		// run the validation with rendering attached as an event listener.
		// The object returned contains any changes made by the validator.
		return processor.validate(parameters);
	}

}
