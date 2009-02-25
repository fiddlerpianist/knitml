package com.knitml.engine.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.knitml.engine.common.KnittingEngineException;


public class FlatRowForwardsTests extends FlatRowTests {

	@Override
	public void onSetUp() throws KnittingEngineException {
		knitter.startNewRow();
		knit(40);
		knitter.startNewRow();
	}

	@Test
	@Override
	public void examineRowCount() throws Exception {
		assertEquals(2, knitter.getCurrentRowNumber());
		knit(40);
		knitter.startNewRow();
		assertEquals(3, knitter.getCurrentRowNumber());
		knit(40);
		knitter.resetRowNumber();
		assertEquals(0, knitter.getCurrentRowNumber());
		knitter.startNewRow();
		assertEquals(1, knitter.getCurrentRowNumber());
	}	


}
