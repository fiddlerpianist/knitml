/**
 * 
 */
package com.knitml.engine.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.knitml.engine.settings.Direction;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class RoundRowTests extends FlatRowTests {

	@Override
	protected void onSetUp() throws Exception {
		engine.declareRoundKnitting();
		engine.startNewRow();
	}

	@Test
	public void knitForTwoRounds() throws Exception {
		knit(40);
		engine.startNewRow();
		knit(40);
		engine.startNewRow();
	}

	@Test
	public void checkNumberOfStitchesInRow() throws Exception {
		assertEquals(40, engine.getTotalNumberOfStitchesInRow());
		assertEquals(40, engine.getStitchesRemainingInRow());
	}

	@Test
	public void checkMarkerPlacement() throws Exception {
		knit(5);
		engine.placeMarker();
		knit(35);
		engine.startNewRow();
		while (engine.getStitchesToNextMarker() > 0) {
			engine.knit();
		}
		assertEquals(35, engine.getStitchesRemainingInRow());
	}

	@Test
	public void switchToFlatKnittingTemporarily() throws Exception {
		knit(5);
		engine.placeMarker();
		knit(35);
		engine.endRow();
		engine.declareFlatKnitting(Direction.FORWARDS);
		engine.startNewRow();
		knit(40);
		engine.startNewRow(); // now we're knitting on the wrong side
		while (engine.getStitchesToNextMarker() > 0) {
			engine.knit();
		}
		assertEquals(5, engine.getStitchesRemainingInRow());
	}
	
	@Test
	@Override
	public void knitLongRow() throws Exception {
		knit(40);
		engine.endRow();
		engine.startNewLongRow();
		knit(45);
		engine.endRow();
	}

}
