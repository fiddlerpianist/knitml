package com.knitml.el

import static com.knitml.el.KelUtils.*
import static org.custommonkey.xmlunit.XMLAssert.*

import org.custommonkey.xmlunit.XMLUnit
import org.junit.BeforeClass
import org.junit.Test

class LanguageMPTests {
	
	@BeforeClass
	static void setUp() {
		initXMLUnit()
	}
	
	@Test
	void ppso() {
		String actual = toXml ("ppso 3")
		String expected = '''
        <pass-previous-stitch-over>3</pass-previous-stitch-over>
        '''
        assertXMLEqual expected, actual
	}
	
	@Test
	void ppsoNoArgs() {
		String actual = toXml ("ppso")
		String expected = '<pass-previous-stitch-over/>'
        assertXMLEqual expected, actual
	}
	
}