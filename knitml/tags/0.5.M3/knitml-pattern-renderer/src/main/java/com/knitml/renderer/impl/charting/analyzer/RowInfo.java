package com.knitml.renderer.impl.charting.analyzer;

public class RowInfo {
	
	private int rowWidth = 0;
//	private int noStitchLeftSideCount = 0;
//	private int noStitchRightSideCount = 0;
//	private int increaseLeftSideCount = 0;
//	private int increaseRightSideCount = 0;

	public int getRowWidth() {
		return rowWidth;
	}
	public void addToRowWidth(int rowWidthToAdd) {
		this.rowWidth += rowWidthToAdd;
	}
	
	public void setRowWidth(int rowWidth) {
		this.rowWidth = rowWidth;
	}

}
