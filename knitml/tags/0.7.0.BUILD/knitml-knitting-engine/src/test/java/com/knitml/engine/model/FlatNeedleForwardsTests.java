package com.knitml.engine.model;

import com.knitml.engine.common.KnittingEngineException;



public class FlatNeedleForwardsTests extends FlatNeedleTests {

	@Override
	public void onSetUp() throws KnittingEngineException {
		nextRow();
		purl(10);
		nextRow();
	}



}
