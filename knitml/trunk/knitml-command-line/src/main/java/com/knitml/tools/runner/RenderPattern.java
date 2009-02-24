package com.knitml.tools.runner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.knitml.core.common.Parameters;
import com.knitml.renderer.RendererProgram;
import com.knitml.renderer.context.RenderingContextFactory;
import com.knitml.renderer.context.impl.SpringRenderingContextFactory;
import com.knitml.renderer.visitor.impl.DefaultVisitorFactory;
import com.knitml.tools.runner.support.RunnerUtils;
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;
import com.knitml.validation.visitor.instruction.impl.SpringVisitorFactory;

public class RenderPattern {

	private final static Logger log = LoggerFactory
			.getLogger(RenderPattern.class);

	private static final Options options = new Options();

	static {
		Option checksyntax = new Option("checksyntax", false,
				"check the syntax against the KnitML schema");
		Option output = new Option("output", true,
				"File name to output the results");
		output.setType("file");
		options.addOption(checksyntax);
		options.addOption(output);
	}

	public static void main(String[] args) {
		// create the parser
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			ApplicationContext applicationContext = null;
			String[] applicationContextFiles = line
					.getOptionValues("applicationContextFiles");
			if (applicationContextFiles == null) {
				applicationContextFiles = new String[] { "applicationContext-patternRenderer.xml" };
			}
			applicationContext = new ClassPathXmlApplicationContext(
					applicationContextFiles);
			Parameters parameters = RunnerUtils.toParameters(line);

			RenderingContextFactory contextFactory = new SpringRenderingContextFactory(
					applicationContext);
			RendererProgram renderer = new RendererProgram(contextFactory,
					new DefaultVisitorFactory(),
					new DefaultKnittingContextFactory(),
					new SpringVisitorFactory());
			renderer.render(parameters);
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			HelpFormatter help = new HelpFormatter();
			help.printHelp("renderPattern [options] filename", options);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			ex.printStackTrace();
		}
	}
}
