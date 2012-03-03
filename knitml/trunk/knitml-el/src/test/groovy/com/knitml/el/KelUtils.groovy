package com.knitml.el

import static com.knitml.core.model.pattern.Version.getCurrentVersionId
import static com.knitml.core.xml.Schemas.CURRENT_PATTERN_SCHEMA

import javax.xml.parsers.DocumentBuilderFactory

import org.apache.commons.lang.StringUtils
import org.custommonkey.xmlunit.XMLUnit

import com.knitml.core.model.pattern.Parameters;
import com.knitml.core.xml.PluggableSchemaResolver

class KelUtils {
	
	protected static String toXml(String kel) {
		def parameters = new Parameters()
		parameters.reader = new StringReader("Pattern { " + kel + " }")
		parameters.writer = new StringWriter()
		KelProgram converter = new KelProgram()
		converter.convertToXml(parameters)
		String value = parameters.writer.toString()
		value = StringUtils.remove(value, '<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.knitml.com/schema/pattern http://www.knitml.com/schema/pattern-0.6.xsd http://www.knitml.com/schema/operations http://www.knitml.com/schema/pattern-0.6.xsd http://www.knitml.com/schema/common http://www.knitml.com/schema/pattern-0.6.xsd" version="0.6">')
		value = StringUtils.remove(value, '</pattern:pattern>')
		return value
	}
	
	public static void initXMLUnit() {
		DocumentBuilderFactory testDocumentBuilderFactory = XMLUnit.testDocumentBuilderFactory
		testDocumentBuilderFactory.namespaceAware = false
		DocumentBuilderFactory controlDocumentBuilderFactory = XMLUnit.controlDocumentBuilderFactory
		controlDocumentBuilderFactory.namespaceAware = false
		XMLUnit.ignoreWhitespace = true
		XMLUnit.ignoreComments = true
	}

	public static void initXMLUnitWithNamespace() {
		DocumentBuilderFactory testDocumentBuilderFactory = XMLUnit.testDocumentBuilderFactory
		testDocumentBuilderFactory.namespaceAware = true
		DocumentBuilderFactory controlDocumentBuilderFactory = XMLUnit.controlDocumentBuilderFactory
		controlDocumentBuilderFactory.namespaceAware = true
		XMLUnit.controlEntityResolver = new PluggableSchemaResolver(KelUtils.class.classLoader)
		XMLUnit.testEntityResolver = new PluggableSchemaResolver(KelUtils.class.classLoader)
		XMLUnit.ignoreWhitespace = true
		XMLUnit.ignoreComments = true
	}
}