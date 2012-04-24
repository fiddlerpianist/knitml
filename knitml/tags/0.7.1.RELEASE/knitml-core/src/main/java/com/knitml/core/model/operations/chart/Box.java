package com.knitml.core.model.operations.chart;

public class Box implements ChartAnnotation {

	protected String id;
	protected String rgbColor;
	protected Integer rowSpan;
	protected Integer stitchSpan;
	protected Integer startRow;
	protected Integer startStitch;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRgbColor() {
		return rgbColor;
	}
	public void setRgbColor(String rgbColor) {
		this.rgbColor = rgbColor;
	}
	public Integer getRowSpan() {
		return rowSpan;
	}
	public void setRowSpan(Integer rowSpan) {
		this.rowSpan = rowSpan;
	}
	public Integer getStitchSpan() {
		return stitchSpan;
	}
	public void setStitchSpan(Integer stitchSpan) {
		this.stitchSpan = stitchSpan;
	}
	public Integer getStartRow() {
		return startRow;
	}
	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}
	public Integer getStartStitch() {
		return startStitch;
	}
	public void setStartStitch(Integer startStitch) {
		this.startStitch = startStitch;
	}
	
}
