package com.knitml.engine;

import com.knitml.core.model.operations.StitchNature;

public interface Stitch extends Restorable {

	String getId();
	StitchNature getCurrentNature();
	void recordNature(StitchNature nature);

}