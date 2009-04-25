/**
 * 
 */
package com.knitml.core.units;

import static com.knitml.core.units.Units.NEEDLE_SIZE_MM;
import static com.knitml.core.units.Units.NEEDLE_SIZE_US;
import static com.knitml.core.units.Units.ROWS_PER_CENTIMETER;
import static com.knitml.core.units.Units.ROWS_PER_INCH;
import static com.knitml.core.units.Units.STITCHES_PER_CENTIMETER;
import static com.knitml.core.units.Units.STITCHES_PER_INCH;
import static com.knitml.core.units.Units.WRAPS_PER_CENTIMETER;
import static com.knitml.core.units.Units.WRAPS_PER_INCH;
import static org.junit.Assert.assertEquals;

import javax.measure.Measurable;
import javax.measure.Measure;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Length;

import org.junit.Test;


public class MeasurementTests {
	@Test
	public void ensureKnittingGaugesPrintCorrectly() throws Exception {
		// 3 inches
		Measurable<? extends Gauge> gauge = KnittingMeasure.valueOf("3", STITCHES_PER_INCH);
		assertEquals("3 st/in", gauge.toString());
		gauge = KnittingMeasure.valueOf("3", ROWS_PER_INCH);
		assertEquals("3 row/in", gauge.toString());
		gauge = KnittingMeasure.valueOf(3, STITCHES_PER_CENTIMETER);
		assertEquals("3 st/cm", gauge.toString());
		gauge = KnittingMeasure.valueOf(3, ROWS_PER_CENTIMETER);
		assertEquals("3 row/cm", gauge.toString());
	}
	
	@Test
	public void ensureYarnThicknessPrintsCorrectly() throws Exception {
		Measurable<YarnThickness> thickness = KnittingMeasure.valueOf("10", WRAPS_PER_INCH);
		assertEquals("10 wrap/in", thickness.toString());
		thickness = KnittingMeasure.valueOf("4", WRAPS_PER_CENTIMETER);
		assertEquals("4 wrap/cm", thickness.toString());
	}
	
	@Test
	public void ensureKnittingNeedleSizesPrintCorrectly() throws Exception {
		// 3 inches
		Measurable<Length> needleSize = KnittingMeasure.valueOf("000", NEEDLE_SIZE_US);
		assertEquals("000 US", needleSize.toString());
		needleSize = KnittingMeasure.valueOf("1.5", NEEDLE_SIZE_MM);
		assertEquals("1.5 mm", needleSize.toString());
	}
	
	@Test
	public void convertStitchesPerUnitValues() throws Exception {
		UnitConverter converter = STITCHES_PER_INCH.getConverterTo(STITCHES_PER_CENTIMETER);
		assertEquals(4, Math.round(converter.convert(10)));
		converter = STITCHES_PER_CENTIMETER.getConverterTo(STITCHES_PER_INCH);
		assertEquals(25, Math.round(converter.convert(10)));
	}
	
	@Test
	public void convertUSNeedle000ToMetric() throws Exception {
		Measure<String, Length> needleSizeUs = KnittingMeasure.valueOf("000", NEEDLE_SIZE_US);
		Measure<String, Length> needleSizeMm = needleSizeUs.to(NEEDLE_SIZE_MM);
		assertEquals(1.5d, needleSizeMm.doubleValue(NEEDLE_SIZE_MM), 0);
	}

	@Test
	public void convertUSNeedle00ToMetric() throws Exception {
		Measure<String, Length> needleSizeUs = KnittingMeasure.valueOf("00", NEEDLE_SIZE_US);
		Measure<String, Length> needleSizeMm = needleSizeUs.to(NEEDLE_SIZE_MM);
		assertEquals(1.75d, needleSizeMm.doubleValue(NEEDLE_SIZE_MM), 0);
	}

	@Test
	public void convertUSNeedle1Point5ToMetric() throws Exception {
		Measure<String, Length> needleSizeUs = KnittingMeasure.valueOf("1.5", NEEDLE_SIZE_US);
		Measure<String, Length> needleSizeMm = needleSizeUs.to(NEEDLE_SIZE_MM);
		assertEquals(2.5d, needleSizeMm.doubleValue(NEEDLE_SIZE_MM), 0);
	}
	
	@Test
	public void convertUSNeedle10ToMetric() throws Exception {
		Measure<String, Length> needleSizeUs = KnittingMeasure.valueOf("10", NEEDLE_SIZE_US);
		Measure<String, Length> needleSizeMm = needleSizeUs.to(NEEDLE_SIZE_MM);
		assertEquals(6.00d, needleSizeMm.doubleValue(NEEDLE_SIZE_MM), 0);
	}
	
	@Test
	public void convertMetricNeedleToUS000Needle() throws Exception {
		Measure<String, Length> needleSizeMm = KnittingMeasure.valueOf("1.5", NEEDLE_SIZE_MM);
		Measure<String, Length> needleSizeUs = needleSizeMm.to(NEEDLE_SIZE_US);
		assertEquals("000", needleSizeUs.getValue());
	}

	@Test
	public void convertMetricNeedleToUS00Needle() throws Exception {
		Measure<String, Length> needleSizeMm = KnittingMeasure.valueOf("1.75", NEEDLE_SIZE_MM);
		Measure<String, Length> needleSizeUs = needleSizeMm.to(NEEDLE_SIZE_US);
		assertEquals("00", needleSizeUs.getValue());
	}

	@Test
	public void convertMetricNeedleToUS10Needle() throws Exception {
		Measure<String, Length> needleSizeMm = KnittingMeasure.valueOf("6", NEEDLE_SIZE_MM);
		Measure<String, Length> needleSizeUs = needleSizeMm.to(NEEDLE_SIZE_US);
		assertEquals("10", needleSizeUs.getValue());
	}

	@Test
	public void convertMetricNeedleToUS1Point5Needle() throws Exception {
		Measure<String, Length> needleSizeMm = KnittingMeasure.valueOf("2.5", NEEDLE_SIZE_MM);
		Measure<String, Length> needleSizeUs = needleSizeMm.to(NEEDLE_SIZE_US);
		assertEquals("1.5", needleSizeUs.getValue());
	}
	
}
