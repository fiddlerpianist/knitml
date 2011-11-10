package com.knitml.el

import static com.knitml.el.KelUtils.toXml
import static org.custommonkey.xmlunit.XMLAssert.*

import org.custommonkey.xmlunit.XMLUnit
import org.junit.BeforeClass
import org.junit.Test

class LanguageVZTests {
	
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
		XMLUnit.ignoreComments = true
	}
	
	@Test
	void workEven() {
		String actual = toXml ("work even")
		String expected = '<work-even/>'
		assertXMLEqual expected, actual
	}
	
	@Test
	void workEvenFiveStitches() {
		String actual = toXml ("work even 5 sts")
		String expected = '<work-even>5</work-even>'
		assertXMLEqual expected, actual
	}
	
	@Test
	void workEvenForNextFiveStitches() {
		String actual = toXml ("work even for next 5 sts")
		String expected = '<work-even>5</work-even>'
		assertXMLEqual expected, actual
	}
	
	@Test
	void workNextFiveStitchesEvenly() {
		String actual = toXml ("work next 5 sts evenly")
		String expected = '<work-even>5</work-even>'
		assertXMLEqual expected, actual
	}
	
	@Test
	void workEvenToEnd() {
		String actual = toXml ("work even to end")
		String expected = '<repeat until="end"><work-even/></repeat>'
		assertXMLEqual expected, actual
	}
	
	@Test
	void workEvenToThreeBeforeEnd() {
		String actual = toXml ("work even to 3 sts before end")
		String expected = '<repeat until="before-end" value="3"><work-even/></repeat>'
		assertXMLEqual expected, actual
	}
	
	@Test
	void workEvenToThreeBeforeEndStitchHolder() {
		// make sure that nonsense noise is filtered out
		String actual = toXml ("work even to 3 sts before end to holder next")
		String expected = '<repeat until="before-end" value="3"><work-even/></repeat>'
		assertXMLEqual expected, actual
	}
}