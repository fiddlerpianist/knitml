package com.knitml.core.model.directions.inline;

import org.apache.commons.lang.ObjectUtils;

import com.knitml.core.common.LoopToWork;
import com.knitml.core.model.directions.StitchNature;


public class Purl extends Knit {

	public Purl(Integer numberOfTimes, String yarnIdRef, LoopToWork loopToWork) {
		super(numberOfTimes, yarnIdRef, loopToWork);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Purl) {
			Knit that = (Knit) obj;
			return (ObjectUtils.equals(this.numberOfTimes, that.numberOfTimes)
					&& ObjectUtils.equals(this.yarnIdRef, that.yarnIdRef) && ObjectUtils
					.equals(this.loopToWork, that.loopToWork));
		}
		return false;
	}

	@Override
	public String toString() {
		return "Purl" + (numberOfTimes != null ? " " + numberOfTimes : "")
				+ (yarnIdRef != null ? " with yarn " + yarnIdRef : "")
				+ (loopToWork != null ? " through " + loopToWork + " loop": "");
	}
	
	@Override
	public StitchNature getStitchNatureProduced() {
		return StitchNature.PURL;
	}

}
