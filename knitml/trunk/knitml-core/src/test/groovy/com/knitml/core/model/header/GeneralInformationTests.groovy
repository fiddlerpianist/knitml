/**
 * 
 */
package com.knitml.core.model.header

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static test.support.JiBXTestUtils.marshalXmlAndCompare
import static test.support.JiBXTestUtils.unmarshalXml

import javax.measure.Measure

import org.custommonkey.xmlunit.XMLUnit
import org.junit.BeforeClass
import org.junit.Test

import com.knitml.core.model.pattern.Pattern;
import com.knitml.core.units.Units

class GeneralInformationTests {
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
	}
	@Test
	void pimpedOutGeneralInformation() {
		def xml = '''
		<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			<pattern:general-information xml:lang="en">
				<pattern:name>Basic socks</pattern:name>
				<pattern:description>A basic sock pattern with 2x2 ribbing</pattern:description>
				<pattern:dimensions>8 inches by 11 inches</pattern:dimensions>
				<pattern:gauge type="stockinette">
					<pattern:stitch-gauge unit="st/in">8</pattern:stitch-gauge>
					<pattern:row-gauge unit="row/in">12</pattern:row-gauge>
				</pattern:gauge>
				<pattern:techniques>
					<pattern:technique>knitting-in-the-round</pattern:technique>
				</pattern:techniques>
				<pattern:author>
					<pattern:first-name>Jonathan</pattern:first-name>
					<pattern:last-name>Whitall</pattern:last-name>
				</pattern:author>
				<pattern:copyright-info>Copyright 2008</pattern:copyright-info>
			</pattern:general-information>
		</pattern:pattern>
		'''
		Pattern pattern = unmarshalXml(xml)
		def genInfo = pattern.generalInformation
		genInfo.with {
			assertThat patternName, is ('Basic socks')
			assertThat description, is ('A basic sock pattern with 2x2 ribbing')
			assertThat dimensions, is ('8 inches by 11 inches')
			gauge.with {
			assertThat swatchType, is ('stockinette')
				stitchGauge.with {
					assertThat (it instanceof Measure, is (true))
					assertThat Integer.valueOf(value), is (8)
					assertThat unit, is (Units.STITCHES_PER_INCH)
				}
				rowGauge.with {
					assertThat (it instanceof Measure, is (true))
					assertThat Integer.valueOf(value), is (12)
					assertThat unit, is (Units.ROWS_PER_INCH)
				}
			}
			assertThat techniques[0], is ('knitting-in-the-round')
			author.with {
				assertThat firstName, is ('Jonathan')
				assertThat lastName, is ('Whitall')
			}
			assertThat copyright, is ('Copyright 2008')
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void minimalGeneralInformation() {
		def xml = '''
		<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			<pattern:general-information/>
		</pattern:pattern>
		'''
		Pattern pattern = unmarshalXml(xml)
		marshalXmlAndCompare(pattern,xml)
	}
	
}
