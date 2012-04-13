package com.knitml.engine.impl;

/**
 * Represents where a certain stitch resides on the knitted work. Includes
 * both needle and stitch coordinates. It will be used to designate where
 * the start of the row is, but for now
 * 
 */
class StitchCoordinate {

	private int needleIndex = 0;
	private int stitchIndex = 0;

	public StitchCoordinate() {
	}

	public StitchCoordinate(int needleIndex, int stitchIndex) {
		this.needleIndex = needleIndex;
		this.stitchIndex = stitchIndex;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof StitchCoordinate)
				&& ((StitchCoordinate) obj).getNeedleIndex() == this.needleIndex
				&& ((StitchCoordinate) obj).getStitchIndex() == this.stitchIndex;
	}

	public int getNeedleIndex() {
		return needleIndex;
	}

	public int getStitchIndex() {
		return stitchIndex;
	}
}