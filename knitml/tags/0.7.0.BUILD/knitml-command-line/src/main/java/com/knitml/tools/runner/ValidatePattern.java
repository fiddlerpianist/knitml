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

import com.knitml.core.model.pattern.Parameters;
import com.knitml.tools.runner.support.RunnerUtils;
import com.knitml.validation.ValidationProgram;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;
import com.knitml.validation.visitor.instruction.impl.DefaultVisitorFactory;

public class ValidatePattern {

	@SuppressWarnings("unused")
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
			// configures input, output, and checksyntax options
			Parameters parameters = RunnerUtils.toParameters(line);
			KnittingContextFactory contextFactory = new DefaultKnittingContextFactory();
			// create the ValidationProgram
			ValidationProgram validator = new ValidationProgram(contextFactory,
					new DefaultVisitorFactory());
			// ValidationProgram validator = new ValidationProgram(new
			// DefaultKnittingContextFactory(),
			// new SpringVisitorFactory());
			validator.validate(parameters);
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			HelpFormatter help = new HelpFormatter();
			help.printHelp("renderPattern [options] filename", options);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
