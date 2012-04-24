package com.knitml.renderer.impl.charting.analyzer;

import org.apache.commons.lang.math.Range;

import com.knitml.core.model.operations.inline.Repeat;

public class RepeatMetaData {

	private Repeat repeat = null;
	private Range repeatRange = null;

	public RepeatMetaData(Repeat repeat, Range repeatRange) {
		super();
		this.repeat = repeat;
		this.repeatRange = repeatRange;
	}

	public Repeat getRepeat() {
		return repeat;
	}

	public Range getRepeatRange() {
		return repeatRange;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((repeat == null) ? 0 : repeat.hashCode());
		result = prime * result
				+ ((repeatRange == null) ? 0 : repeatRange.hashCode());
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
		RepeatMetaData other = (RepeatMetaData) obj;
		if (repeat == null) {
			if (other.repeat != null)
				return false;
		} else {
			if (other.repeat == null)
				return false;
			if (repeat.getUntil() != other.repeat.getUntil())
				return false;
			if (repeat.getValue() == null) {
				if (other.repeat.getValue() != null)
					return false;
			} else {
				if (other.repeat.getValue() == null)
					return false;
				if (!(repeat.getValue()).equals(other.repeat.getValue()))
					return false;
			}
		}
		if (repeatRange == null) {
			if (other.repeatRange != null)
				return false;
		} else if (!repeatRange.equals(other.repeatRange))
			return false;
		return true;
	}

}
