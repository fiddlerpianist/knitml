package com.knitml.tools.runner

import groovy.xml.MarkupBuilder

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.GnuParser

import com.knitml.tools.runner.support.RunnerUtils
import com.knitml.el.KelProgram

class ConvertPattern {

	private static final options = new Options()
	private static final Logger log = LoggerFactory.getLogger(ConvertPattern)
	
	static {
		def checksyntax = new Option("checksyntax", false,"check the syntax against the KnitML schema")
		def output = new Option("output", true,"File name to output the results")
		output.setType("file")
		options.addOption(checksyntax)
		options.addOption(output)
	}
	
	static void main(args) {

		// create the parser
		CommandLineParser parser = new GnuParser()
		// parse the command line arguments
		CommandLine line = parser.parse(options, args)
		// we're not using an applicationContext for conversion, so pass null
		def options = RunnerUtils.toParameters(line)
		KelProgram converter = new KelProgram()
		converter.convertToXml(options)
	}
	
}
