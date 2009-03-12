/**
 * 
 */
package com.knitml.engine.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.knitml.core.model.directions.inline.Increase;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.common.NotEndOfRowException;
import com.knitml.engine.common.NotEnoughStitchesException;
import com.knitml.engine.common.StitchesAlreadyOnNeedleException;
import com.knitml.engine.impl.DefaultKnittingEngine;
import com.knitml.engine.impl.DefaultKnittingFactory;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public abstract class FlatRowTests {

	protected DefaultKnittingEngine knitter;

	protected void knit(int numberOfStitches) throws KnittingEngineException {
		knitter.knit(numberOfStitches);
	}

	protected void purl(int numberOfStitches) throws KnittingEngineException {
		knitter.purl(numberOfStitches);
	}

	/**
	 * Cast on 40 stitches onto one needle. A DefaultKnittingEngine
	 * starts out knitting flat in the forwards direction.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		knitter = new DefaultKnittingEngine(new DefaultKnittingFactory());
		knitter.castOn(40);
		onSetUp();
	}

	protected abstract void onSetUp() throws Exception;

	@Test
	public void knitToEndOfRow() throws Exception {
		knit(40);
		assertEquals(40, knitter.getTotalNumberOfStitchesInRow());
		assertTrue(knitter.isEndOfRow());
		knitter.endRow();
	}

	@Test(expected = NotEnoughStitchesException.class)
	public void knitPastEndOfRow() throws Exception {
		knit(41);
	}

	/**
	 * Verify that we're starting at the beginning of the row.
	 * 
	 * @throws Exception
	 */
	@Test
	public void checkBeginningOfRow() throws Exception {
		assertTrue(knitter.isBeginningOfRow());
	}

	/**
	 * Verify that, after we work one stitch, we're no longer at the beginning
	 * of the row.
	 * 
	 * @throws Exception
	 */
	@Test
	public void checkNotBeginningOfRow() throws Exception {
		knit(1);
		assertFalse(knitter.isBeginningOfRow());
	}

	/**
	 * Knit to the end of the row, then assert that we are at the end of the row
	 * by every means possible.
	 * 
	 * @throws Exception
	 */
	@Test
	public void verifyEndOfRow() throws Exception {
		knit(40);
		assertEquals(0, knitter.getStitchesRemainingInRow());
		assertEquals(40, knitter.getTotalNumberOfStitchesInRow());
		assertTrue(knitter.isEndOfRow());
		knitter.endRow();
	}

	/**
	 * Attempt to assert that we are at the end of the row, when in reality we
	 * are not.
	 * 
	 * @throws Exception
	 */
	@Test(expected = NotEndOfRowException.class)
	public void misAssertEndOfRow() throws Exception {
		knit(30);
		knitter.endRow();
	}

	/**
	 * Attempt to start a new row when the current row has not been completed.
	 * 
	 * @throws Exception
	 */
	@Test(expected = NotEndOfRowException.class)
	public void misStartNewRow() throws Exception {
		knit(30);
		knitter.startNewRow();
	}

	/**
	 * Knit to 5 stitches before the end, turn, knit to 7 stitches before the
	 * beginning, turn, knit to end. Assert end of row.
	 * 
	 * @throws Exception
	 */
	@Test
	public void knitAndTurn() throws Exception {
		knit(35);
		assertEquals(5, knitter.getStitchesRemainingInRow());
		knitter.turn();
		knit(28);
		assertEquals(7, knitter.getStitchesRemainingInRow());
		knitter.turn();
		knit(33);
		knitter.endRow();
	}

	/**
	 * Row 1: K21, place marker, K19.
	 * Row 2: K40 (neutralizes flat or round knitting shape).
	 * Start new row, then assert that there are 21 stitches before the marker. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void placeMarker() throws Exception {
		knit(21);
		knitter.placeMarker();
		knit(19);
		knitter.startNewRow();
		knit(40);
		knitter.startNewRow();
		assertEquals(21, knitter.getStitchesToNextMarker());
	}

	/**
	 * Slip 30. Assert there are 10 stitches left to work. 
	 * Slip back 30. Assert there are 40 stitches left to work.
	 * 
	 * @throws Exception
	 */
	@Test
	public void slipAroundTheRow() throws Exception {
		for (int i = 0; i < 30; i++) {
			knitter.slip();
		}
		assertEquals(10, knitter.getStitchesRemainingInRow());

		for (int i = 0; i < 30; i++) {
			knitter.reverseSlip();
		}
		assertEquals(40, knitter.getStitchesRemainingInRow());
	}

	@Test(expected=NotEnoughStitchesException.class)
	public void slipBackTooFar() throws Exception {
		knitter.slip();
		knitter.reverseSlip();
		knitter.reverseSlip();
	}
	
	@Test
	public void identifyPlaceInRow() throws Exception {
		assertTrue(knitter.isBeginningOfRow());
		assertFalse(knitter.isEndOfRow());
		knit(5);
		assertFalse(knitter.isBeginningOfRow());
		assertFalse(knitter.isEndOfRow());
		knit(35);
		assertFalse(knitter.isBeginningOfRow());
		assertTrue(knitter.isEndOfRow());
	}
	
	@Test
	public void examineRowCount() throws Exception {
		assertEquals(1, knitter.getCurrentRowNumber());
		knit(40);
		knitter.startNewRow();
		assertEquals(2, knitter.getCurrentRowNumber());
		knit(40);
		knitter.resetRowNumber();
		assertEquals(0, knitter.getCurrentRowNumber());
		knitter.startNewRow();
		assertEquals(1, knitter.getCurrentRowNumber());
	}	
	
	public void resetRowNumberInMiddleOfRow() throws Exception {
		knit(20);
		knitter.resetRowNumber();
	}
	
	@Test
	public void getStitchesRemainingOnCurrentNeedle() throws Exception {
		knit(20);
		assertEquals(20, knitter.getStitchesRemainingOnCurrentNeedle());
	}
	
	@Test
	public void getTotalNumberOfStitchesOnCurrentNeedle() throws Exception {
		assertEquals(40, knitter.getTotalNumberOfStitchesOnCurrentNeedle());
		assertEquals(40, knitter.getTotalNumberOfStitchesInRow());
	}
	
	@Test(expected=StitchesAlreadyOnNeedleException.class)
	public void castOnInMiddleOfRow() throws Exception {
		knit(25);
		knitter.castOn(5);
	}
	
	@Test
	public void knitAndPurlTwoTogether() throws Exception {
		knitter.knit();
		knitter.knitTwoTogether();
		knitter.knit();
		knitter.knitTwoTogether();
		assertEquals(34, knitter.getStitchesRemainingInRow());
		assertEquals(38, knitter.getTotalNumberOfStitchesInRow());
		knit(34);
		
		knitter.startNewRow();
		knitter.purl();
		knitter.purlTwoTogether();
		knitter.purl();
		knitter.purlTwoTogether();
		assertEquals(32, knitter.getStitchesRemainingInRow());
		assertEquals(36, knitter.getTotalNumberOfStitchesInRow());
	}

	@Test
	public void lacePattern() throws Exception {
		knitter.knit();
		knitter.knitTwoTogether();
		knitter.knit();
		knitter.increase(new Increase(1));
		assertEquals(36, knitter.getStitchesRemainingInRow());
		assertEquals(40, knitter.getTotalNumberOfStitchesInRow());
		knit(36);
		
		knitter.startNewRow();
		knitter.purlTwoTogether();
		knitter.purlTwoTogether();
		knitter.increase(new Increase(2));
		assertEquals(36, knitter.getStitchesRemainingInRow());
		assertEquals(40, knitter.getTotalNumberOfStitchesInRow());
		knit(36);
	}
	
	@Test
	public void bindOffAllStitchesInRow() throws Exception {
		knit(1);
		int numberToBindOff = knitter.getStitchesRemainingInRow() - 1;
		for (int i = 0; i < numberToBindOff; i++) {
			knit(1);
			knitter.passPreviousStitchOver();
		}
	}


}
