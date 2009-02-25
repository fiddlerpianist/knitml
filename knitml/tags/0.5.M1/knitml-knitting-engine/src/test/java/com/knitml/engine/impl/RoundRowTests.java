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
		knitter.declareRoundKnitting();
		knitter.startNewRow();
	}

	@Test
	public void knitForTwoRounds() throws Exception {
		knit(40);
		knitter.startNewRow();
		knit(40);
		knitter.startNewRow();
	}

	@Test
	public void checkNumberOfStitchesInRow() throws Exception {
		assertEquals(40, knitter.getTotalNumberOfStitchesInRow());
		assertEquals(40, knitter.getStitchesRemainingInRow());
	}

	@Test
	public void checkMarkerPlacement() throws Exception {
		knit(5);
		knitter.placeMarker();
		knit(35);
		knitter.startNewRow();
		while (knitter.getStitchesToNextMarker() > 0) {
			knitter.knit();
		}
		assertEquals(35, knitter.getStitchesRemainingInRow());
	}

	@Test
	public void switchToFlatKnittingTemporarily() throws Exception {
		knit(5);
		knitter.placeMarker();
		knit(35);
		knitter.endRow();
		knitter.declareFlatKnitting(Direction.FORWARDS);
		knitter.startNewRow();
		knit(40);
		knitter.startNewRow(); // now we're knitting on the wrong side
		while (knitter.getStitchesToNextMarker() > 0) {
			knitter.knit();
		}
		assertEquals(5, knitter.getStitchesRemainingInRow());
	}

}
