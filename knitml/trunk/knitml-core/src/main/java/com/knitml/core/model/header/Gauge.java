/**
 * 
 */
package com.knitml.core.model.header;

import javax.measure.Measurable;

import com.knitml.core.units.RowGauge;
import com.knitml.core.units.StitchGauge;

public class Gauge {
	protected String swatchType;
	protected Measurable<RowGauge> rowGauge;
	protected Measurable<StitchGauge> stitchGauge;
	
	public Gauge(String swatchType, Measurable<RowGauge> rowGauge,
			Measurable<StitchGauge> stitchGauge) {
		super();
		this.swatchType = swatchType;
		this.rowGauge = rowGauge;
		this.stitchGauge = stitchGauge;
	}
	
	public Measurable<RowGauge> getRowGauge() {
		return rowGauge;
	}
	public Measurable<StitchGauge> getStitchGauge() {
		return stitchGauge;
	}
	public String getSwatchType() {
		return swatchType;
	}
	public void setSwatchType(String swatchType) {
		this.swatchType = swatchType;
	}
	public void setRowGauge(Measurable<RowGauge> rowGauge) {
		this.rowGauge = rowGauge;
	}
	public void setStitchGauge(Measurable<StitchGauge> stitchGauge) {
		this.stitchGauge = stitchGauge;
	}
	
	
}