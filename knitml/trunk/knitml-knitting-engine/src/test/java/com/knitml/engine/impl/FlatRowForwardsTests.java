package com.knitml.engine.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.knitml.engine.common.KnittingEngineException;


public class FlatRowForwardsTests extends FlatRowTests {

	@Override
	public void onSetUp() throws KnittingEngineException {
		engine.startNewRow();
		knit(40);
		engine.startNewRow();
	}

	@Test
	@Override
	public void examineRowCount() throws Exception {
		assertEquals(2, engine.getCurrentRowNumber());
		knit(40);
		engine.startNewRow();
		assertEquals(3, engine.getCurrentRowNumber());
		knit(40);
		engine.resetRowNumber();
		assertEquals(0, engine.getCurrentRowNumber());
		engine.startNewRow();
		assertEquals(1, engine.getCurrentRowNumber());
	}	


}
