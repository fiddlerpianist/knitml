package com.knitml.tools.runner


import javax.xml.validation.*
import javax.xml.transform.*


import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.GnuParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.jibx.runtime.BindingDirectory
import org.jibx.runtime.IBindingFactory
import org.jibx.runtime.IUnmarshallingContext

import com.knitml.core.model.Pattern

import com.knitml.core.common.Parameters
import com.knitml.el.KelProgram
import com.knitml.tools.runner.support.RunnerUtils
import com.knitml.validation.ValidationProgram;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;
import com.knitml.validation.visitor.instruction.impl.SpringVisitorFactory;

class ConvertAndValidatePattern {

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
		
		// convert the document
		def parameters = new Parameters()
		parameters.reader = RunnerUtils.getReader(line) 
		parameters.checkSyntax = RunnerUtils.getCheckSyntax(line)
		// convert to XML document
		KelProgram converter = new KelProgram()
		String xml = converter.convertToXml(parameters)

		IBindingFactory factory = BindingDirectory.getFactory(Pattern)
		IUnmarshallingContext uctx = factory.createUnmarshallingContext()
		Pattern pattern = uctx.unmarshalDocument(new StringReader(xml))
		
		// now validate the document
		parameters = new Parameters()
		parameters.pattern = pattern
		parameters.writer = RunnerUtils.getWriter(line)
		
		KnittingContextFactory contextFactory = new DefaultKnittingContextFactory()
		ValidationProgram validator = new ValidationProgram(contextFactory, new SpringVisitorFactory())
		validator.validate(parameters)
	}
		
}
