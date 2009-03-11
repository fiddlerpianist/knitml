package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringEndsWith.endsWith
import static org.junit.Assert.assertThat

import org.apache.commons.lang.StringUtils

import org.jibx.runtime.JiBXException
import org.junit.Test
import org.junit.Before
import org.junit.Ignore
import org.junit.runner.JUnitCore

import com.knitml.core.units.Units
import com.knitml.core.model.header.GeneralInformation
import com.knitml.core.common.ValidationException

import test.support.AbstractRenderingContextTests

class SquareGaugeVisitorTests extends AbstractRenderingContextTests {
	
	private static final LINE_BREAK = System.getProperty("line.separator");

	@Before
	void setSquare() {
		renderingContext.options.squareGauge = true
	}
	
	@Test
	void squareGaugeWithInches() {
		processXml '''
		  <general-information xmlns="http://www.knitml.com/schema/pattern">
		  <gauge>
			<stitch-gauge unit="st/in">5</stitch-gauge>
			<row-gauge unit="row/in">8</row-gauge>
          </gauge>
          </general-information>''', GeneralInformation
		assertThat output.trim(), endsWith ("Gauge:  20 sts, 32 rows = 4 in")
	}

	@Test
	void squareGaugeWithInchesRenderedWithImperial() {
		renderingContext.options.useImperialUnits()
		processXml '''
		  <general-information xmlns="http://www.knitml.com/schema/pattern">
		  <gauge>
			<stitch-gauge unit="st/in">5</stitch-gauge>
			<row-gauge unit="row/in">8</row-gauge>
          </gauge>
          </general-information>''', GeneralInformation
		assertThat output.trim(), endsWith ("Gauge:  20 sts, 32 rows = 4 in")
	}
	
	@Test
	void squareGaugeWithInchesRenderedWithMetric() {
		renderingContext.options.useInternationalUnits()
		processXml '''
		  <general-information xmlns="http://www.knitml.com/schema/pattern">
		  <gauge>
			<stitch-gauge unit="st/in">5</stitch-gauge>
			<row-gauge unit="row/in">8</row-gauge>
          </gauge>
          </general-information>''', GeneralInformation
		assertThat output.trim(), endsWith ("Gauge:  20 sts, 31 rows = 10 cm")
	}
	
	@Test
	void squareGaugeWithCentimeters() {
		processXml '''
		  <general-information xmlns="http://www.knitml.com/schema/pattern">
		  <gauge>
			<stitch-gauge unit="st/cm">2</stitch-gauge>
			<row-gauge unit="row/cm">3.2</row-gauge>
          </gauge>
          </general-information>''', GeneralInformation
		assertThat output.trim(), endsWith ("Gauge:  20 sts, 32 rows = 10 cm")
	}
	
	@Test
	void squareGaugeWithCentimetersRenderedWithImperial() {
		renderingContext.options.useImperialUnits()
		processXml '''
		  <general-information xmlns="http://www.knitml.com/schema/pattern">
		  <gauge>
			<stitch-gauge unit="st/cm">2</stitch-gauge>
			<row-gauge unit="row/cm">3.2</row-gauge>
          </gauge>
          </general-information>''', GeneralInformation
		assertThat output.trim(), endsWith ("Gauge:  20 sts, 33 rows = 4 in")
	}
	
	@Test
	void squareGaugeWithMillimeters() {
		processXml '''
		  <general-information xmlns="http://www.knitml.com/schema/pattern">
		  <gauge>
			<stitch-gauge unit="st/mm">0.2</stitch-gauge>
          </gauge>
          </general-information>''', GeneralInformation
		assertThat output.trim(), endsWith ("Gauge:  0 sts = 1 mm")
	}

	@Test
	void squareGaugeWithMillimetersRenderedInCentimeters() {
		renderingContext.options.useInternationalUnits()
		processXml '''
		  <general-information xmlns="http://www.knitml.com/schema/pattern">
		  <gauge>
			<stitch-gauge unit="st/mm">0.2</stitch-gauge>
          </gauge>
          </general-information>''', GeneralInformation
		assertThat output.trim(), endsWith ("Gauge:  20 sts = 10 cm")
	}
	
	@Test
	void squareGaugeWithMixedSystemOfUnits() {
		processXml '''
		  <general-information xmlns="http://www.knitml.com/schema/pattern">
		  <gauge>
			<stitch-gauge unit="st/in">5</stitch-gauge>
			<row-gauge unit="row/cm">3.2</row-gauge>
          </gauge>
          </general-information>''', GeneralInformation
		assertThat output.trim(), endsWith ("Gauge:  20 sts, 33 rows = 4 in")
	}
	
	@Test
	void squareGaugeWithMixedSystemOfUnitPreferences1() {
		// shows that the row gauge preference is ignored for square gauges
		// if a stitch gauge is found
		renderingContext.options.stitchGaugeUnit = Units.STITCHES_PER_INCH
		renderingContext.options.rowGaugeUnit = Units.ROWS_PER_CENTIMETER
		
		processXml '''
		  <general-information xmlns="http://www.knitml.com/schema/pattern">
		  <gauge>
			<stitch-gauge unit="st/in">5</stitch-gauge>
			<row-gauge unit="row/cm">3.2</row-gauge>
          </gauge>
          </general-information>''', GeneralInformation
		assertThat output.trim(), endsWith ("Gauge:  20 sts, 33 rows = 4 in")
	}

	@Test
	void squareGaugeWithStitchGaugeOnly() {
		processXml '''
		  <general-information xmlns="http://www.knitml.com/schema/pattern">
		  <gauge>
			<stitch-gauge unit="st/in">5</stitch-gauge>
          </gauge>
          </general-information>''', GeneralInformation
		assertThat output.trim(), endsWith ("Gauge:  20 sts = 4 in")
	}
	
	@Test
	void squareGaugeWithRowGaugeOnly() {
		processXml '''
		  <general-information xmlns="http://www.knitml.com/schema/pattern">
		  <gauge>
			<row-gauge unit="row/cm">3.2</row-gauge>
          </gauge>
          </general-information>''', GeneralInformation
		assertThat output.trim(), endsWith ("Gauge:  32 rows = 10 cm")
	}
	
	static void main(args) {
		JUnitCore.main(SquareGaugeVisitorTests.name)
	}
	
}
