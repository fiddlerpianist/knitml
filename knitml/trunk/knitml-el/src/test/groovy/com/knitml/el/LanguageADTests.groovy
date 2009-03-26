package com.knitml.el

import org.junit.Test
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.junit.runner.JUnitCore
import org.junit.internal.runners.JUnit4ClassRunner
import org.custommonkey.xmlunit.XMLUnit

import com.knitml.core.common.Parameters
import com.knitml.el.GroovyKnitProgram

import static org.custommonkey.xmlunit.XMLAssert.*
import static com.knitml.el.KelUtils.toXml

@RunWith(JUnit4ClassRunner)
class LanguageADTests {
	
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
		XMLUnit.ignoreComments = true
	}
	
	@Test
	void pattern() {
		String actual = toXml( '''
			Pattern 'en' {}
		''')
		String expected = '''<pattern xml:lang="en" />'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void kToEnd() {
		String actual = toXml ("k to 2 sts before end")
		String expected = '''
			<repeat until="before-end" value="2">
				<knit/>
			</repeat>'''
        assertXMLEqual expected, actual
	}

	@Test
	void applyNextRow() {
		String actual = toXml ("applyNextRow 'my'")
		String expected = '''<apply-next-row instruction-ref="my"/>'''
        assertXMLEqual expected, actual
	}

	@Test
	void arrangeStitches() {
		String actual = toXml ("arrangeStitches 'n1':10 'n2':12")
		String expected = '''<arrange-stitches-on-needles>
                               <needle ref="n1">10</needle>
                               <needle ref="n2">12</needle>
                             </arrange-stitches-on-needles>'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void ballWeight() {
		String actual = toXml ("ballWeight: 50 g")
		String expected = '''<ball-weight unit="g">50</ball-weight>'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void bindOff1() {
		String actual = toXml ("bindOff 10 sts 'y1' purlwise")
		String expected = '''<bind-off yarn-ref="y1" type="purlwise">10
                             </bind-off>'''
        assertXMLEqual expected, actual
	}

	@Test
	void bindOff1a() {
		String actual = toXml ("bindOff 10")
		String expected = '''<bind-off>10</bind-off>'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void bindOff2() {
		String actual = toXml ("bindOff all sts knitwise")
		String expected = '''<bind-off-all type="knitwise"/>'''
        assertXMLEqual expected, actual
	}

	@Test
	void bindOff3() {
		String actual = toXml ("bindOff allStitches knitwise")
		String expected = '''<bind-off-all type="knitwise"/>'''
        assertXMLEqual expected, actual
	}

	@Test
	void bindOff4() {
		String actual = toXml ("bindOff all")
		String expected = '''<bind-off-all/>'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void bo() {
		String actual = toXml ("bo 10")
		String expected = '''<bind-off>10</bind-off>'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void castOn1() {
		String actual = toXml ("castOn 35 sts")
		String expected = '''<cast-on>35</cast-on>'''
        assertXMLEqual expected, actual
	}

	@Test
	void castOn2() {
		String actual = toXml ("castOn 35")
		String expected = '''<cast-on>35</cast-on>'''
        assertXMLEqual expected, actual
	}

	@Test
	void co1() {
		String actual = toXml ("co 35")
		String expected = '''<cast-on>35</cast-on>'''
        assertXMLEqual expected, actual
	}

	@Test
	void co2() {
		String actual = toXml ("co 35 st")
		String expected = '''<cast-on>35</cast-on>'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void color() {
		String actual = toXml ("color 'yellow' [number:127]")
		String expected = '''<color name="yellow" number="127"/>'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void copyrightInfo() {
		String actual = toXml ("copyrightInfo")
		String expected = '''<copyright-info />'''
        assertXMLEqual expected, actual
	}

	@Test
	void cross1() {
		String actual = toXml ("cross 2 inFrontOf 2")
		String expected = '''<cross-stitches first="2" next="2" type="front" />'''
        assertXMLEqual expected, actual
	}

	@Test
	void cross2() {
		String actual = toXml ("cross 2 behind 2")
		String expected = '''<cross-stitches first="2" next="2" type="back" />'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void crossStitches() {
		String actual = toXml ("crossStitches 2 inFrontOf 2")
		String expected = '''<cross-stitches first="2" next="2" type="front" />'''
        assertXMLEqual expected, actual
	}

	@Test
	void declareFlatKnitting() {
		String actual = toXml ("declareFlatKnitting with rightSide next")
		String expected = '''<declare-flat-knitting next-row-side="right"/>'''
        assertXMLEqual expected, actual
	}

	@Test
	void declareRoundKnitting() {
		String actual = toXml ("declareRoundKnitting")
		String expected = '''<declare-round-knitting/>'''
        assertXMLEqual expected, actual
	}

	@Test
	void designateEndOfRow() {
		String actual = toXml ("designateEndOfRow")
		String expected = '''<designate-end-of-row/>'''
        assertXMLEqual expected, actual
	}

	@Test
	void directions() {
		String actual = toXml ("directions {}")
		String expected = '''<directions />'''
        assertXMLEqual expected, actual
	}

	@Test
	void directives() {
		String actual = toXml ("directives {}")
		String expected = '''<directives />'''
        assertXMLEqual expected, actual
	}
	
	static void main(args) {
		JUnitCore.main(LanguageADTests.name)
	}
}