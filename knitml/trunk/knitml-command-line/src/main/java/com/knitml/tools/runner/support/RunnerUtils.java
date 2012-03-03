package com.knitml.tools.runner.support;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;

import com.knitml.core.model.pattern.Parameters;


public class RunnerUtils {

	public static Parameters toParameters(CommandLine line) throws IOException, MissingArgumentException {
		Parameters parameters = new Parameters();
		parameters.setCheckSyntax(line.hasOption("checksyntax"));
		parameters.setWriter(getWriter(line));
		parameters.setReader(getReader(line));
		return parameters;
	}
	
	public static Writer getWriter(CommandLine line) throws IOException {
		String outputFileName = line.getOptionValue("output");
		if (outputFileName != null) {
			return new BufferedWriter(new FileWriter(outputFileName));
		} else {
			return new BufferedWriter(new OutputStreamWriter(System.out));
		}
	}

	public static Reader getReader(CommandLine line) throws IOException, MissingArgumentException {
		String[] leftoverArgs = line.getArgs();
		if (leftoverArgs.length != 1) {
			throw new MissingArgumentException("Missing filename to process, which is required");
		}
		return new FileReader(leftoverArgs[0]);
	}
	
	public static boolean getCheckSyntax(CommandLine line) {
		return line.hasOption("checksyntax");
	}
}
