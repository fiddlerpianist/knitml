package com.knitml.renderer;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.jibx.runtime.JiBXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.knitml.core.common.Parameters;
import com.knitml.core.model.Pattern;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.context.RenderingContextFactory;
import com.knitml.renderer.context.impl.DefaultRenderingContextFactory;
import com.knitml.renderer.event.EventFactory;
import com.knitml.renderer.event.impl.DefaultEventFactory;
import com.knitml.renderer.listener.RenderingListenerAdapter;
import com.knitml.validation.ValidationProgram;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.PatternEventListener;

// TODO this initialization is mind-numbingly complex because these classes were never meant to work together like this
public class RendererProgram {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(RendererProgram.class);

	// default if not passed
	private RenderingContextFactory renderingCF = new DefaultRenderingContextFactory();
	private EventFactory renderingVF = new DefaultEventFactory();
	private KnittingContextFactory knittingCF;
	private com.knitml.validation.visitor.instruction.VisitorFactory knittingVF;

	public RendererProgram() {
	}

	public RendererProgram(RenderingContextFactory renderingCF,
			EventFactory renderingVF) {
		this.renderingCF = renderingCF;
		this.renderingVF = renderingVF;
	}

	public RendererProgram(RenderingContextFactory renderingCF,
			EventFactory renderingVF, KnittingContextFactory knittingCF,
			com.knitml.validation.visitor.instruction.VisitorFactory knittingVF) {
		this.renderingCF = renderingCF;
		this.renderingVF = renderingVF;
		this.knittingCF = knittingCF;
		this.knittingVF = knittingVF;
	}

	public Pattern render(Parameters parameters) throws SAXException,
			JiBXException, IOException, RenderingException {

		RenderingContext renderingContext = renderingCF
				.createRenderingContext();
		RenderingListenerAdapter listener = new RenderingListenerAdapter(
				renderingContext);
		ValidationProgram processor;
		if (knittingCF == null || knittingVF == null) {
			processor = new ValidationProgram(listener, true);
		} else {
			List<PatternEventListener> listeners = new ArrayList<PatternEventListener>();
			listeners.add(listener);
			processor = new ValidationProgram(knittingCF, knittingVF, listeners);
		}

		Writer writer = parameters.getWriter();
		if (writer != null) {
			renderingContext.getRenderer().setWriter(writer);
			// the writer is on the listener adapter, not on the processing
			// program
			parameters.setWriter(null);
		}
		return processor.validate(parameters);
	}

}
