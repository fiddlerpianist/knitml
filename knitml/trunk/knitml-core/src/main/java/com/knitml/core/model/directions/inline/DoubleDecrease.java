package com.knitml.core.model.directions.inline;

public class DoubleDecrease extends Decrease {

	@Override
	public int getAdvanceCount() {
		int numberOfDecreases = numberOfTimes == null ? 1 : numberOfTimes;
		return 3 * numberOfDecreases;
	}
	@Override
	public int getIncreaseCount() {
		int numberOfDecreases = numberOfTimes == null ? 1 : numberOfTimes;
		return -2 * numberOfDecreases;
	}
}
