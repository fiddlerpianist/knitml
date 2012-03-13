package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.SlipDirection;
import com.knitml.core.common.Wise;
import com.knitml.core.common.YarnPosition;
import com.knitml.core.model.operations.DiscreteInlineOperation;

public class Slip implements DiscreteInlineOperation {

	protected Integer numberOfTimes;
	protected Wise type;
	protected YarnPosition yarnPosition;
	protected SlipDirection direction;

	public Slip() {
	}

	public Slip(int numberOfTimes) {
		this.numberOfTimes = numberOfTimes;
	}

	public Slip(Integer numberOfTimes, Wise type, YarnPosition yarnPosition,
			SlipDirection direction) {
		this.numberOfTimes = numberOfTimes;
		this.type = type;
		this.yarnPosition = yarnPosition;
		this.direction = direction;
	}

	public List<Slip> canonicalize() {
		int size = numberOfTimes == null ? 1 : numberOfTimes;
		List<Slip> newOps = new ArrayList<Slip>(size);
		for (int i = 0; i < size; i++) {
			newOps.add(new Slip(1, this.type == null ? Wise.PURLWISE
					: this.type, this.yarnPosition == null ? YarnPosition.BACK
					: this.yarnPosition,
					this.direction == null ? SlipDirection.FORWARD
							: this.direction));
		}
		return newOps;
	}

	public Integer getNumberOfTimes() {
		return numberOfTimes;
	}

	public Wise getType() {
		return type;
	}

	public YarnPosition getYarnPosition() {
		return yarnPosition;
	}

	public int getAdvanceCount() {
		return numberOfTimes == null ? 1 : numberOfTimes;
	}

	public int getIncreaseCount() {
		return 0;
	}

	public SlipDirection getDirection() {
		return direction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result
				+ ((numberOfTimes == null) ? 0 : numberOfTimes.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((yarnPosition == null) ? 0 : yarnPosition.hashCode());
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
		Slip other = (Slip) obj;
		if (direction != other.direction)
			return false;
		if (numberOfTimes == null) {
			if (other.numberOfTimes != null)
				return false;
		} else if (!numberOfTimes.equals(other.numberOfTimes))
			return false;
		if (type != other.type)
			return false;
		if (yarnPosition != other.yarnPosition)
			return false;
		return true;
	}

}
