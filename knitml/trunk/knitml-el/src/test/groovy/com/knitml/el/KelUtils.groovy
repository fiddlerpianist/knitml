package com.knitml.el

import org.apache.commons.lang.StringUtilsimport com.knitml.core.common.Parameters
import com.knitml.el.KelProgram

import static com.knitml.core.xml.Schemas.CURRENT_PATTERN_SCHEMA
import static com.knitml.core.model.Version.getCurrentVersionId

class KelUtils {
	
	protected static String toXml(String kel) {
		def parameters = new Parameters()
		parameters.reader = new StringReader("Pattern { " + kel + " }")
		parameters.writer = new StringWriter()
		KelProgram converter = new KelProgram()
		converter.convertToXml(parameters)
		String value = parameters.writer.toString()
		value = StringUtils.remove(value, '<pattern xmlns="http://www.knitml.com/schema/pattern" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.knitml.com/schema/pattern ' + CURRENT_PATTERN_SCHEMA + '" version="' + getCurrentVersionId() + '">')
		value = StringUtils.remove(value, '</pattern>')
		return value
	}
}