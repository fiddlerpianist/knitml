/**
 * 
 */
package com.knitml.engine.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.knitml.engine.common.CannotAdvanceNeedleException;
import com.knitml.engine.settings.Direction;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class RoundRowMultiNeedleTests extends FlatRowMultiNeedleTests {
	@Test
	public void knitForTwoRounds() throws Exception {
		knit(40);
		knitter.startNewRow();
		knit(40);
		knitter.startNewRow();
	}

	@Test
	public void checkNumberOfNeedles() throws Exception {
		assertEquals(3, knitter.getNumberOfNeedles());
	}

	@Test
	public void checkNumberOfStitchesInRow() throws Exception {
		assertEquals(40, knitter.getTotalNumberOfStitchesInRow());
		assertEquals(40, knitter.getStitchesRemainingInRow());
	}

	@Test
	public void checkMarkerPlacementOnFirstNeedle() throws Exception {
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
	public void checkMarkerPlacementOnLastNeedle() throws Exception {
		knit(35);
		knitter.placeMarker();
		knit(5);
		knitter.startNewRow();
		while (knitter.getStitchesToNextMarker() > 0) {
			knitter.knit();
		}
		assertEquals(5, knitter.getStitchesRemainingInRow());
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

	@Test
	public void knitByNeedle() throws Exception {
		assertEquals(10, knitter.getStitchesRemainingOnCurrentNeedle());
		knit(10);
		knitter.advanceNeedle();
		assertEquals(10, knitter.getStitchesRemainingOnCurrentNeedle());
		knit(10);
		knitter.advanceNeedle();
		assertEquals(20, knitter.getStitchesRemainingOnCurrentNeedle());
		knit(20);
		knitter.endRow();
	}

	@Test(expected=CannotAdvanceNeedleException.class)
	public void advanceNeedleIncorrectly() throws Exception {
		assertEquals(10, knitter.getStitchesRemainingOnCurrentNeedle());
		knit(9);
		knitter.advanceNeedle();
	}
	
	@Test
	public void declareEndOfRowInMiddle() throws Exception {
		knit(25);
		knitter.designateEndOfRow();
		knitter.arrangeStitchesOnNeedles(new int[] { 15, 5, 20 });
		knitter.startNewRow();
		assertEquals("needle3", knitter.getCurrentNeedle().getId());
		assertEquals(15, knitter.getStitchesRemainingOnCurrentNeedle());
		assertEquals(40, knitter.getStitchesRemainingInRow());
		knit(40);
		knitter.endRow();
	}

	@Test
	public void declareEndOfRowAtEnd() throws Exception {
		knit(40);
		knitter.designateEndOfRow();
		knitter.startNewRow();
		assertEquals(40, knitter.getStitchesRemainingInRow());
		knit(40);
		knitter.endRow();
	}

	@Test
	public void declareEndOfRowAtBeginning() throws Exception {
		knitter.designateEndOfRow();
		knitter.startNewRow();
		assertEquals(40, knitter.getStitchesRemainingInRow());
		knit(40);
		knitter.endRow();
	}

	@Override
	protected void onSetUp() throws Exception {
		knitter.declareRoundKnitting();
		knitter.startNewRow();
	}

}
