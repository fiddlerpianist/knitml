package com.knitml.renderer.context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.springframework.context.HierarchicalMessageSource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.ResourceLoader;

import com.knitml.core.model.operations.block.Instruction;
import com.knitml.core.units.RowGauge;
import com.knitml.core.units.StitchGauge;
import com.knitml.core.units.Units;

/**
 * Preferences set on the RenderingContext to guide the renderer in decisions
 * required in the rendering process.
 * 
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class Options {

	// global system preferences
	private Locale locale = Locale.getDefault();
	private ResourceLoader patternMessageResourceLoader = new FileSystemResourceLoader();
	private HierarchicalMessageSource programMessageSource = null;
	
	// global rendering preferences
	private boolean globalChart = false;
	private boolean squareGauge = false;
	private boolean greyNoStitches = true;

	private Unit<StitchGauge> stitchGaugeUnit;
	private Unit<RowGauge> rowGaugeUnit;
	private Unit<Length> fabricMeasurementUnit;
	private String[] fontNames;

	// Override global preferences for the particular instruction ID
	private Map<String, InstructionOption> instructionOptions = new HashMap<String, InstructionOption>();
	
	public boolean isGlobalChart() {
		return globalChart;
	}
	
	public void setGlobalChart(boolean chart) {
		this.globalChart = chart;
	}

	public boolean shouldChart(String instructionId) {
		InstructionOption option = instructionOptions.get(instructionId);
		if (option != null) {
			return option.isChart();
		}
		return globalChart;
	}

	public boolean shouldChart(Instruction instruction) {
		return shouldChart(instruction.getId());
	}

	public Map<String, InstructionOption> getInstructionOptions() {
		return instructionOptions;
	}

	public InstructionOption getInstructionOption(String key) {
		return instructionOptions.get(key);
	}

	public void addInstructionOption(String id,
			InstructionOption instructionOption) {
		this.instructionOptions.put(id, instructionOption);
	}


	public void useInternationalUnits() {
		this.stitchGaugeUnit = Units.STITCHES_PER_CENTIMETER;
		this.rowGaugeUnit = Units.ROWS_PER_CENTIMETER;
		this.fabricMeasurementUnit = Units.CENTIMETER;
	}

	public void useUsCustomaryUnits() {
		this.stitchGaugeUnit = Units.STITCHES_PER_INCH;
		this.rowGaugeUnit = Units.ROWS_PER_INCH;
		this.fabricMeasurementUnit = Units.INCH;
	}

	public Unit<StitchGauge> getStitchGaugeUnit() {
		return stitchGaugeUnit;
	}

	public void setStitchGaugeUnit(Unit<StitchGauge> stitchGaugeUnit) {
		this.stitchGaugeUnit = stitchGaugeUnit;
	}

	public Unit<RowGauge> getRowGaugeUnit() {
		return rowGaugeUnit;
	}

	public void setRowGaugeUnit(Unit<RowGauge> rowGaugeUnit) {
		this.rowGaugeUnit = rowGaugeUnit;
	}

	public Unit<Length> getFabricMeasurementUnit() {
		return fabricMeasurementUnit;
	}

	public void setFabricMeasurementUnit(Unit<Length> fabricMeasurementUnit) {
		this.fabricMeasurementUnit = fabricMeasurementUnit;
	}

	public boolean isSquareGauge() {
		return squareGauge;
	}

	public void setSquareGauge(boolean squareGauge) {
		this.squareGauge = squareGauge;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public ResourceLoader getPatternMessageResourceLoader() {
		return patternMessageResourceLoader;
	}

	public void setPatternMessageResourceLoader(
			ResourceLoader messageSourceResourceLoader) {
		this.patternMessageResourceLoader = messageSourceResourceLoader;
	}

	public HierarchicalMessageSource getProgramMessageSource() {
		return programMessageSource;
	}

	public void setProgramMessageSource(HierarchicalMessageSource programMessageSource) {
		this.programMessageSource = programMessageSource;
	}

	public String[] getFontNames() {
		return fontNames;
	}

	public void setFontNames(String[] fontNames) {
		this.fontNames = fontNames;
	}

	public boolean isGreyNoStitches() {
		return greyNoStitches;
	}

	public void setGreyNoStitches(boolean greyNoStitches) {
		this.greyNoStitches = greyNoStitches;
	}
}
