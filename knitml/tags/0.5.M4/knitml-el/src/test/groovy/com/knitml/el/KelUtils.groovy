package com.knitml.el

import org.apache.commons.lang.StringUtilsimport com.knitml.core.common.Parameters
import com.knitml.el.GroovyKnitProgram

class KelUtils {
	
	protected static String toXml(String kel) {
		def parameters = new Parameters()
		parameters.reader = new StringReader("Pattern { " + kel + " }")
		parameters.writer = new StringWriter()
		GroovyKnitProgram converter = new GroovyKnitProgram()
		converter.convertToXml(parameters)
		String value = parameters.writer.toString()
		value = StringUtils.remove(value, '<pattern xmlns="http://www.knitml.com/schema/pattern" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.knitml.com/schema/pattern http://www.knitml.com/schema/pattern-0.5.xsd" version="0.5">')
		value = StringUtils.remove(value, '</pattern>')
		return value
	}
}