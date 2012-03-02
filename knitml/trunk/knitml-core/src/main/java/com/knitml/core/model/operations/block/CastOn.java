package com.knitml.core.model.operations.block;

import com.knitml.core.model.operations.BlockOperation;

public class CastOn implements BlockOperation {

	protected Integer numberOfStitches;
	protected String yarnIdRef;
	protected String style;
	protected boolean countAsRow;

	public CastOn() {
	}
	
	public CastOn(Integer numberOfStitches, String yarnIdRef, String style) {
		this.numberOfStitches = numberOfStitches;
		this.yarnIdRef = yarnIdRef;
		this.style = style;
	}

	public boolean isCountAsRow() {
		return countAsRow;
	}

	public void setCountAsRow(boolean countAsRow) {
		this.countAsRow = countAsRow;
	}

	public Integer getNumberOfStitches() {
		return numberOfStitches;
	}

	public String getYarnIdRef() {
		return yarnIdRef;
	}

	public String getStyle() {
		return style;
	}

	public void setNumberOfStitches(Integer numberOfStitches) {
		this.numberOfStitches = numberOfStitches;
	}

	public void setYarnIdRef(String yarnIdRef) {
		this.yarnIdRef = yarnIdRef;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (countAsRow ? 1231 : 1237);
		result = prime
				* result
				+ ((numberOfStitches == null) ? 0 : numberOfStitches.hashCode());
		result = prime * result + ((style == null) ? 0 : style.hashCode());
		result = prime * result
				+ ((yarnIdRef == null) ? 0 : yarnIdRef.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CastOn other = (CastOn) obj;
		if (countAsRow != other.countAsRow)
			return false;
		if (numberOfStitches == null) {
			if (other.numberOfStitches != null)
				return false;
		} else if (!numberOfStitches.equals(other.numberOfStitches))
			return false;
		if (style == null) {
			if (other.style != null)
				return false;
		} else if (!style.equals(other.style))
			return false;
		if (yarnIdRef == null) {
			if (other.yarnIdRef != null)
				return false;
		} else if (!yarnIdRef.equals(other.yarnIdRef))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CastOn [numberOfStitches:" + numberOfStitches + ",yarnIdRef:"
				+ yarnIdRef + ",style:" + style + ",countAsRow:" + countAsRow
				+ "]";
	}

}
