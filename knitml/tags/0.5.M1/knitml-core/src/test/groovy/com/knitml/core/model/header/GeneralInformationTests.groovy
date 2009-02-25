/**
 * 
 */
package com.knitml.core.model.header

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static test.support.JiBXTestUtils.unmarshalXml
import static test.support.JiBXTestUtils.marshalXmlAndCompare

import javax.measure.Measure

import org.custommonkey.xmlunit.XMLUnit

import org.junit.Test
import org.junit.Ignore
import org.junit.BeforeClass
import org.junit.internal.runners.JUnit4ClassRunner
import org.junit.runner.JUnitCore
import org.junit.runner.RunWith

import com.knitml.core.units.Units
import com.knitml.core.model.Pattern
import com.knitml.core.common.Side
import com.knitml.core.common.Wise
import com.knitml.core.common.LoopToWork
import com.knitml.core.common.YarnPosition
import com.knitml.core.common.NeedleStyle

@RunWith(JUnit4ClassRunner)
class GeneralInformationTests {
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
	}
	@Test
	void pimpedOutGeneralInformation() {
		def xml = '''
		<pattern xmlns="http://www.knitml.com/schema/pattern">
			<general-information xml:lang="en">
				<name>Basic socks</name>
				<description>A basic sock pattern with 2x2 ribbing</description>
				<dimensions>8 inches by 11 inches</dimensions>
				<gauge type="stockinette">
					<stitch-gauge unit="st/in">8</stitch-gauge>
					<row-gauge unit="row/in">12</row-gauge>
				</gauge>
				<techniques>
					<technique>knitting-in-the-round</technique>
				</techniques>
				<author>
					<first-name>Jonathan</first-name>
					<last-name>Whitall</last-name>
				</author>
				<copyright-info>Copyright 2008</copyright-info>
			</general-information>
		</pattern>
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
		<pattern xmlns="http://www.knitml.com/schema/pattern">
			<general-information/>
		</pattern>
		'''
		Pattern pattern = unmarshalXml(xml)
		marshalXmlAndCompare(pattern,xml)
	}
	
	static void main(args) {
		JUnitCore.main(GeneralInformationTests.name)
	}
	
}
