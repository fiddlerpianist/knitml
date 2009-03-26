package com.knitml.tools.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateAndRenderPattern {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(RenderPattern.class);

//	private static final Options options = new Options();
//
//	static {
//		Option checksyntax = new Option("checksyntax", false,
//				"check the syntax against the KnitML schema");
//		Option output = new Option("output", true,
//				"File name to output the results");
//		Option writeTempFile = new Option("wv", false,
//				"Write validated file to a local file before rendering");
//		output.setType("file");
//		options.addOption(checksyntax);
//		options.addOption(output);
//		options.addOption(writeTempFile);
//	}

	public static void main(String[] args) {
		RenderPattern.main(args);
//		// create the parser
//		CommandLineParser parser = new GnuParser();
//		try {
//			// parse the command line arguments
//			CommandLine line = parser.parse(options, args);
//			Pattern pattern = runValidation(line);
//			//Document document = toDocument(pattern);
//			runRendering(line, pattern);
//
//		} catch (ParseException exp) {
//			// oops, something went wrong
//			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
//			HelpFormatter help = new HelpFormatter();
//			help.printHelp("validateAndRenderPattern [options] filename",
//					options);
//		} catch (Exception ex) {
//			throw new RuntimeException(ex);
//		}
	}

//	private static Pattern runValidation(CommandLine line) throws IOException,
//			MissingArgumentException, SAXException, JiBXException,
//			KnittingEngineException {
//		KnittingContextFactory validationContextFactory = new DefaultKnittingContextFactory();
//		VisitorFactory validationVisitorFactory = new SpringVisitorFactory();
//
//		Parameters parameters = new Parameters();
//		// get the Reader from the filename argument to process
//		parameters.setReader(RunnerUtils.getReader(line));
//		// If -wv is specified, write out the validated KnitML file to a
//		// temp file
//		if (line.hasOption("wv")) {
//			File tempFile = obtainTemporaryFile();
//			parameters.setWriter(new BufferedWriter(new FileWriter(tempFile)));
//		}
//		ValidationProgram validator = new ValidationProgram(
//				validationContextFactory, validationVisitorFactory);
//		return validator.validate(parameters);
//	}
//
//	private static void runRendering(CommandLine line, Pattern pattern)
//			throws IOException, RenderingException, SAXException,
//			JiBXException {
//		String[] applicationContextFiles = line
//				.getOptionValues("rendererConfigFiles");
//		if (applicationContextFiles == null) {
//			applicationContextFiles = new String[] { "applicationContext-patternRenderer.xml" };
//		}
//		RenderingContextFactory contextFactory = new SpringRenderingContextFactory(
//				applicationContextFiles);
//		com.knitml.renderer.visitor.VisitorFactory visitorFactory = new com.knitml.renderer.visitor.impl.DefaultVisitorFactory();
//
//		Parameters parameters = new Parameters();
//		parameters.setPattern(pattern);
//		parameters.setWriter(RunnerUtils.getWriter(line));
//		// run the program
//		RendererProgram renderer = new RendererProgram(contextFactory,
//				visitorFactory);
//		renderer.render(parameters);
//	}
//
//	private static File obtainTemporaryFile() {
//		return new File("pattern-validated.xml");
//	}
}
