package com.knitml.core.model.directions.inline;

import org.apache.commons.lang.ObjectUtils;

import com.knitml.core.common.LoopToWork;


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
	
}
