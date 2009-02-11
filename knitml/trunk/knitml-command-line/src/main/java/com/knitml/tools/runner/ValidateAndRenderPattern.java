package com.knitml.tools.runner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jibx.runtime.JiBXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.knitml.core.common.Parameters;
import com.knitml.core.model.Pattern;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.renderer.RendererProgram;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.RenderingContextFactory;
import com.knitml.renderer.context.impl.SpringRenderingContextFactory;
import com.knitml.tools.runner.support.RunnerUtils;
import com.knitml.validation.ValidationProgram;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;
import com.knitml.validation.visitor.instruction.VisitorFactory;
import com.knitml.validation.visitor.instruction.impl.SpringVisitorFactory;

public class ValidateAndRenderPattern {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(RenderPattern.class);

	private static final Options options = new Options();

	static {
		Option checksyntax = new Option("checksyntax", false,
				"check the syntax against the KnitML schema");
		Option output = new Option("output", true,
				"File name to output the results");
		Option writeTempFile = new Option("wv", false,
				"Write validated file to a local file before rendering");
		output.setType("file");
		options.addOption(checksyntax);
		options.addOption(output);
		options.addOption(writeTempFile);
	}

	public static void main(String[] args) {
		// create the parser
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			Pattern pattern = runValidation(line);
			//Document document = toDocument(pattern);
			runRendering(line, pattern);

		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			HelpFormatter help = new HelpFormatter();
			help.printHelp("validateAndRenderPattern [options] filename",
					options);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private static Pattern runValidation(CommandLine line) throws IOException,
			MissingArgumentException, SAXException, JiBXException,
			KnittingEngineException {
		KnittingContextFactory validationContextFactory = new DefaultKnittingContextFactory();
		VisitorFactory validationVisitorFactory = new SpringVisitorFactory();

		Parameters parameters = new Parameters();
		// get the Reader from the filename argument to process
		parameters.setReader(RunnerUtils.getReader(line));
		// If -wv is specified, write out the validated KnitML file to a
		// temp file
		if (line.hasOption("wv")) {
			File tempFile = obtainTemporaryFile();
			parameters.setWriter(new BufferedWriter(new FileWriter(tempFile)));
		}
		ValidationProgram validator = new ValidationProgram(
				validationContextFactory, validationVisitorFactory);
		return validator.validate(parameters);
	}

	private static void runRendering(CommandLine line, Pattern pattern)
			throws IOException, RenderingException, SAXException,
			JiBXException {
		String[] applicationContextFiles = line
				.getOptionValues("rendererConfigFiles");
		if (applicationContextFiles == null) {
			applicationContextFiles = new String[] { "applicationContext-patternRenderer.xml" };
		}
		RenderingContextFactory contextFactory = new SpringRenderingContextFactory(
				applicationContextFiles);
		com.knitml.renderer.visitor.VisitorFactory visitorFactory = new com.knitml.renderer.visitor.impl.DefaultVisitorFactory();

		Parameters parameters = new Parameters();
		parameters.setPattern(pattern);
		parameters.setWriter(RunnerUtils.getWriter(line));
		// run the program
		RendererProgram renderer = new RendererProgram(contextFactory,
				visitorFactory);
		renderer.render(parameters);
	}

	private static File obtainTemporaryFile() {
		return new File("pattern-validated.xml");
	}
}
