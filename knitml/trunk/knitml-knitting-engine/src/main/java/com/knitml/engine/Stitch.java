package com.knitml.engine;

public interface Stitch extends Restorable {

	String getId();
	void recordOperation(StitchOperation operation);

}