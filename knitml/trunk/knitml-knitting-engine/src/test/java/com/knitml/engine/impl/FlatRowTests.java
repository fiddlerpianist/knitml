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
import com.knitml.engine.common.WrongKnittingShapeException;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public abstract class FlatRowTests {

	protected DefaultKnittingEngine engine;

	protected void knit(int numberOfStitches) throws KnittingEngineException {
		engine.knit(numberOfStitches);
	}

	protected void purl(int numberOfStitches) throws KnittingEngineException {
		engine.purl(numberOfStitches);
	}

	/**
	 * Cast on 40 stitches onto one needle. A DefaultKnittingEngine
	 * starts out knitting flat in the forwards direction.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		engine = new DefaultKnittingEngine(new DefaultKnittingFactory());
		engine.castOn(40);
		onSetUp();
	}

	protected abstract void onSetUp() throws Exception;

	@Test
	public void knitToEndOfRow() throws Exception {
		knit(40);
		assertEquals(40, engine.getTotalNumberOfStitchesInRow());
		assertTrue(engine.isEndOfRow());
		engine.endRow();
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
		assertTrue(engine.isBeginningOfRow());
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
		assertFalse(engine.isBeginningOfRow());
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
		assertEquals(0, engine.getStitchesRemainingInRow());
		assertEquals(40, engine.getTotalNumberOfStitchesInRow());
		assertTrue(engine.isEndOfRow());
		engine.endRow();
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
		engine.endRow();
	}

	/**
	 * Attempt to start a new row when the current row has not been completed.
	 * 
	 * @throws Exception
	 */
	@Test(expected = NotEndOfRowException.class)
	public void misStartNewRow() throws Exception {
		knit(30);
		engine.startNewRow();
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
		assertEquals(5, engine.getStitchesRemainingInRow());
		engine.turn();
		knit(28);
		assertEquals(7, engine.getStitchesRemainingInRow());
		engine.turn();
		knit(33);
		engine.endRow();
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
		engine.placeMarker();
		knit(19);
		engine.startNewRow();
		knit(40);
		engine.startNewRow();
		assertEquals(21, engine.getStitchesToNextMarker());
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
			engine.slip();
		}
		assertEquals(10, engine.getStitchesRemainingInRow());

		for (int i = 0; i < 30; i++) {
			engine.reverseSlip();
		}
		assertEquals(40, engine.getStitchesRemainingInRow());
	}

	@Test(expected=NotEnoughStitchesException.class)
	public void slipBackTooFar() throws Exception {
		engine.slip();
		engine.reverseSlip();
		engine.reverseSlip();
	}
	
	@Test
	public void identifyPlaceInRow() throws Exception {
		assertTrue(engine.isBeginningOfRow());
		assertFalse(engine.isEndOfRow());
		knit(5);
		assertFalse(engine.isBeginningOfRow());
		assertFalse(engine.isEndOfRow());
		knit(35);
		assertFalse(engine.isBeginningOfRow());
		assertTrue(engine.isEndOfRow());
	}
	
	@Test
	public void examineRowCount() throws Exception {
		assertEquals(1, engine.getCurrentRowNumber());
		knit(40);
		engine.startNewRow();
		assertEquals(2, engine.getCurrentRowNumber());
		knit(40);
		engine.resetRowNumber();
		assertEquals(0, engine.getCurrentRowNumber());
		engine.startNewRow();
		assertEquals(1, engine.getCurrentRowNumber());
	}	
	
	public void resetRowNumberInMiddleOfRow() throws Exception {
		knit(20);
		engine.resetRowNumber();
	}
	
	@Test
	public void getStitchesRemainingOnCurrentNeedle() throws Exception {
		knit(20);
		assertEquals(20, engine.getStitchesRemainingOnCurrentNeedle());
	}
	
	@Test
	public void getTotalNumberOfStitchesOnCurrentNeedle() throws Exception {
		assertEquals(40, engine.getTotalNumberOfStitchesOnCurrentNeedle());
		assertEquals(40, engine.getTotalNumberOfStitchesInRow());
	}
	
	@Test(expected=StitchesAlreadyOnNeedleException.class)
	public void castOnInMiddleOfRow() throws Exception {
		knit(25);
		engine.castOn(5);
	}
	
	@Test
	public void knitAndPurlTwoTogether() throws Exception {
		engine.knit();
		engine.knitTwoTogether();
		engine.knit();
		engine.knitTwoTogether();
		assertEquals(34, engine.getStitchesRemainingInRow());
		assertEquals(38, engine.getTotalNumberOfStitchesInRow());
		knit(34);
		
		engine.startNewRow();
		engine.purl();
		engine.purlTwoTogether();
		engine.purl();
		engine.purlTwoTogether();
		assertEquals(32, engine.getStitchesRemainingInRow());
		assertEquals(36, engine.getTotalNumberOfStitchesInRow());
	}

	@Test
	public void lacePattern() throws Exception {
		engine.knit();
		engine.knitTwoTogether();
		engine.knit();
		engine.increase(new Increase(1));
		assertEquals(36, engine.getStitchesRemainingInRow());
		assertEquals(40, engine.getTotalNumberOfStitchesInRow());
		knit(36);
		
		engine.startNewRow();
		engine.purlTwoTogether();
		engine.purlTwoTogether();
		engine.increase(new Increase(2));
		assertEquals(36, engine.getStitchesRemainingInRow());
		assertEquals(40, engine.getTotalNumberOfStitchesInRow());
		knit(36);
	}
	
	@Test
	public void bindOffAllStitchesInRow() throws Exception {
		knit(1);
		int numberToBindOff = engine.getStitchesRemainingInRow() - 1;
		for (int i = 0; i < numberToBindOff; i++) {
			knit(1);
			engine.passPreviousStitchOver();
		}
	}

	@Test(expected=WrongKnittingShapeException.class)
	public void knitLongRow() throws Exception {
		knit(40);
		engine.endRow();
		engine.startNewLongRow();
	}
}
