package com.knitml.renderer.context;

import java.util.HashMap;
import java.util.Map;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import com.knitml.core.model.directions.block.Instruction;
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

	// global preferences... can be overridden by instruction-specific preferences
	private boolean globalChart = false;
	
	private boolean squareGauge = false;
	private Unit<StitchGauge> stitchGaugeUnit;
	private Unit<RowGauge> rowGaugeUnit;
	private Unit<Length> fabricMeasurementUnit;

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

	public void useImperialUnits() {
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

}
