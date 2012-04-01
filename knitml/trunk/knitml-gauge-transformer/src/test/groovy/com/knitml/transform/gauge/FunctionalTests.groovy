/**
 * 
 */
package com.knitml.transform.gauge

import static com.knitml.core.units.Units.STITCHES_PER_INCH
import static com.knitml.transform.gauge.Unmarshaller.unmarshal
import static com.knitml.transform.gauge.XmlComparator.marshalAndCompare
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.assertThat

import javax.measure.Measure

import org.custommonkey.xmlunit.XMLUnit
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.core.io.ClassPathResource

import com.knitml.core.model.pattern.Parameters
import com.knitml.core.model.pattern.Pattern
import com.knitml.transform.gauge.event.TransformHandler
import com.knitml.transform.gauge.event.TransformPatternEventListener
import com.knitml.validation.ValidationProgram

class FunctionalTests {
	
	@BeforeClass
	static void setUpXml() {
		XMLUnit.ignoreWhitespace = true
		XMLUnit.ignoreComments = true
	}
	
	ValidationProgram program
	
	@Before
	void setUp() {
		TransformHandler handler = new TransformHandler()
		handler.targetGauge = Measure.valueOf(4, STITCHES_PER_INCH)
		program = new ValidationProgram(new TransformPatternEventListener(handler))
	}
	
	@Test
	void simple() {
		String xml = new ClassPathResource('square.xml').inputStream.text
		Pattern pattern = unmarshal (xml)
		def params = new Parameters()
		params.pattern = pattern
		pattern = program.validate(params)
	    marshalAndCompare (pattern, new ClassPathResource('square-transformed.xml').inputStream.text) 
	}
	
}
