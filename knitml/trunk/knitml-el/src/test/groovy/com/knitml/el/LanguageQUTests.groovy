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
class LanguageQUTests {
	
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
		XMLUnit.ignoreComments = true
	}
	
	@Test
	void repeatInstructionFor3Inches() {
		String actual = toXml ("repeat 'blah' for 3.5 in")
		String expected = '''
			<repeat-instruction ref="blah">
				<until-measures unit="in">3.5</until-measures>
			</repeat-instruction>
		'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void stitchHolder() {
		String actual = toXml ("stitchHolder 'sh1' withKey withLabel 'Stitch Holder 1'")
		String expected = '''
			<stitch-holder id="sh1" message-key="stitch-holder.sh1" label="Stitch Holder 1"/>
		'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void slReverse() {
		String actual = toXml ("sl reverse 2 sts")
		String expected = '''
			<slip direction="reverse">2</slip>
		'''
        assertXMLEqual expected, actual
	}
	@Test
	void slInReverse() {
		String actual = toXml ("sl inReverse 2 sts")
		String expected = '''
			<slip direction="reverse">2</slip>
		'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void sl2Wyib() {
		String actual = toXml ("sl 2 wyib")
		String expected = '''<slip yarn-position="back">2</slip>'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void sl2Wyif() {
		String actual = toXml ("sl 2 wyif")
		String expected = '''<slip yarn-position="front">2</slip>'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void slFromHolder() {
		String actual = toXml ("sl 2 sts from holder 'sh1'")
		String expected = '''
			<from-stitch-holder ref="sh1">
				<slip>2</slip>
			</from-stitch-holder>'''
        assertXMLEqual expected, actual
	}
	@Test
	void slPurlwiseFromHolder() {
		String actual = toXml ("sl2 purlwise from holder 'sh1'")
		String expected = '''
			<from-stitch-holder ref="sh1">
				<slip type="purlwise">2</slip>
			</from-stitch-holder>'''
        assertXMLEqual expected, actual
	}
	@Test
	void sl2ToHolder() {
		String actual = toXml ("sl next 2 sts to holder 'sh1'")
		String expected = '''<slip-to-stitch-holder ref="sh1">2</slip-to-stitch-holder>'''
        assertXMLEqual expected, actual
	}
	
	@Test
	void sl1ToHolder() {
		String actual = toXml ("sl next st to holder 'sh1'")
		String expected = '''<slip-to-stitch-holder ref="sh1">1</slip-to-stitch-holder>'''
        assertXMLEqual expected, actual
	}

	static void main(args) {
		JUnitCore.main(LanguageQUTests.name)
	}
}