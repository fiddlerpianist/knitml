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
		engine.startNewRow();
		knit(40);
		engine.startNewRow();
	}

	@Test
	public void checkNumberOfNeedles() throws Exception {
		assertEquals(3, engine.getNumberOfNeedles());
	}

	@Test
	public void checkNumberOfStitchesInRow() throws Exception {
		assertEquals(40, engine.getTotalNumberOfStitchesInRow());
		assertEquals(40, engine.getStitchesRemainingInRow());
	}

	@Test
	public void checkMarkerPlacementOnFirstNeedle() throws Exception {
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
	public void checkMarkerPlacementOnLastNeedle() throws Exception {
		knit(35);
		engine.placeMarker();
		knit(5);
		engine.startNewRow();
		while (engine.getStitchesToNextMarker() > 0) {
			engine.knit();
		}
		assertEquals(5, engine.getStitchesRemainingInRow());
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
	public void knitByNeedle() throws Exception {
		assertEquals(10, engine.getStitchesRemainingOnCurrentNeedle());
		knit(10);
		engine.advanceNeedle();
		assertEquals(10, engine.getStitchesRemainingOnCurrentNeedle());
		knit(10);
		engine.advanceNeedle();
		assertEquals(20, engine.getStitchesRemainingOnCurrentNeedle());
		knit(20);
		engine.endRow();
	}

	@Test(expected=CannotAdvanceNeedleException.class)
	public void advanceNeedleIncorrectly() throws Exception {
		assertEquals(10, engine.getStitchesRemainingOnCurrentNeedle());
		knit(9);
		engine.advanceNeedle();
	}
	
	@Test
	public void declareEndOfRowInMiddle() throws Exception {
		knit(25);
		engine.designateEndOfRow();
		engine.arrangeStitchesOnNeedles(new int[] { 15, 5, 20 });
		engine.startNewRow();
		assertEquals("needle3", engine.getCurrentNeedle().getId());
		assertEquals(15, engine.getStitchesRemainingOnCurrentNeedle());
		assertEquals(40, engine.getStitchesRemainingInRow());
		knit(40);
		engine.endRow();
	}

	@Test
	public void declareEndOfRowAtEnd() throws Exception {
		knit(40);
		engine.designateEndOfRow();
		engine.startNewRow();
		assertEquals(40, engine.getStitchesRemainingInRow());
		knit(40);
		engine.endRow();
	}

	@Test
	public void declareEndOfRowAtBeginning() throws Exception {
		engine.designateEndOfRow();
		engine.startNewRow();
		assertEquals(40, engine.getStitchesRemainingInRow());
		knit(40);
		engine.endRow();
	}

	@Test
	public void knitLongRowWithKnitTwoTogether() throws Exception {
		knit(40);
		engine.endRow();
		engine.startNewLongRow();
		knit(39);
		// knit together last stitch from this round and first stitch from next round;
		// resulting stitch should go on the last needle of the row
		engine.knitTwoTogether();
		engine.endRow();

		engine.startNewRow();
		assertEquals(9, engine.getStitchesRemainingOnCurrentNeedle());
		knit(9);
		engine.advanceNeedle();
		assertEquals(10, engine.getStitchesRemainingOnCurrentNeedle());
		knit(10);
		engine.advanceNeedle();
		assertEquals(20, engine.getStitchesRemainingOnCurrentNeedle());
		knit(20);
		engine.endRow();
	}
	
	@Test
	@Override
	public void knitLongRow() throws Exception {
		knit(40);
		engine.endRow();
		engine.startNewLongRow();
		knit(45);
		engine.endRow();

		engine.startNewRow();
		assertEquals(5, engine.getStitchesRemainingOnCurrentNeedle());
		knit(5);
		engine.advanceNeedle();
		assertEquals(10, engine.getStitchesRemainingOnCurrentNeedle());
		knit(10);
		engine.advanceNeedle();
		assertEquals(25, engine.getStitchesRemainingOnCurrentNeedle());
		knit(25);
		engine.endRow();
	}
	
	@Override
	protected void onSetUp() throws Exception {
		engine.declareRoundKnitting();
		engine.startNewRow();
	}

}
