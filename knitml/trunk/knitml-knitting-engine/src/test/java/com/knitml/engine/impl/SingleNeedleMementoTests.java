/**
 * 
 */
package com.knitml.engine.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.NeedleStyle;
import com.knitml.core.model.directions.StitchNature;
import com.knitml.engine.KnittingEngine;
import com.knitml.engine.KnittingFactory;
import com.knitml.engine.Needle;
import com.knitml.engine.Stitch;
import com.knitml.engine.common.NoGapFoundException;
import com.knitml.engine.common.NoMarkerFoundException;
import com.knitml.engine.settings.Direction;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class SingleNeedleMementoTests {

	protected KnittingEngine engine;
	protected KnittingFactory knittingFactory = new DefaultKnittingFactory();
	protected Needle initialNeedle = knittingFactory.createNeedle("default",
			NeedleStyle.CIRCULAR);
	private Object memento;

	private void save() throws Exception {
		memento = engine.save();
	}

	private void restore() throws Exception {
		engine.restore(memento);
	}

	@Before
	public final void setUp() throws Exception {
		engine = new DefaultKnittingEngine(knittingFactory);
		List<Needle> needles = new ArrayList<Needle>();
		needles.add(initialNeedle);
		engine.useNeedles(needles);
		engine.castOn(40, true);
		assertOriginalState();
		save();
	}

	@After
	public final void verify() throws Exception {
		restore();
		assertOriginalState();
	}

	private void assertOriginalState() throws Exception {
		// capture state right after the cast-on row before startNewRow() is
		// called
		assertEquals(1, engine.getTotalRowsCompleted());
		assertEquals(1, engine.getCurrentRowNumber());
		assertEquals(40, engine.getTotalNumberOfStitchesInRow());
		assertEquals(0, engine.getStitchesRemainingInRow());
		assertEquals(Direction.FORWARDS, engine.getDirection());
		assertEquals(KnittingShape.FLAT, engine.getKnittingShape());
		assertFalse(engine.isWorkingIntoStitch());
		
		// this will mess up original state, so do a save/restore
		save();
		engine.startNewRow();
		Stitch stitch = engine.peekAtNextStitch();
		assertEquals("n", stitch.getId());
		assertEquals(StitchNature.KNIT, stitch.getCurrentNature());
		restore();
	}

	@Test
	public void checkLastOperationOnStitchRestored() throws Exception {
		engine.startNewRow();
		// backwards
		engine.knit(40);
		engine.startNewRow();
		// forwards
		engine.slip(39);
		// check stitch "n"
		Stitch stitch = engine.peekAtNextStitch();
		assertEquals("n", stitch.getId());
		assertEquals(StitchNature.PURL, stitch.getCurrentNature());
	}

	@Test
	public void checkStitchCountsAndKnittingShapeRestored() throws Exception {
		engine.startNewRow();
		assertEquals(Direction.BACKWARDS, engine.getDirection());
		engine.knit(10);
		for (int i = 0; i < 10; i++) {
			engine.knitTwoTogether();
		}
		engine.knit(10);
		assertEquals(2, engine.getCurrentRowNumber());
		assertEquals(30, engine.getTotalNumberOfStitchesInRow());
		engine.endRow();
		assertEquals(2, engine.getTotalRowsCompleted());
		engine.declareRoundKnitting();
		assertEquals(KnittingShape.ROUND, engine.getKnittingShape());
		engine.startNewRow();
		engine.knit(5);
	}

	@Test(expected = NoMarkerFoundException.class)
	public void checkMarkersRemoved() throws Exception {
		engine.startNewRow();
		engine.knit(10);
		engine.placeMarker();
		engine.knit(15);
		engine.placeMarker();
		engine.knit(15);
		engine.endRow();
		engine.startNewRow();
		assertEquals(15, engine.getStitchesToNextMarker());

		// restore
		restore();
		engine.startNewRow();
		engine.getStitchesToNextMarker();
	}

	@Test(expected = NoGapFoundException.class)
	public void checkGapsRemoved() throws Exception {
		engine.startNewRow();
		engine.knit(10);
		engine.turn();
		engine.knit(10);
		engine.turn();
		assertEquals(10, engine.getStitchesToGap());

		// restore
		restore();
		engine.startNewRow();
		engine.getStitchesToGap();
	}

	@Test
	public void checkRowPositionReset() throws Exception {
		engine.startNewRow();
		engine.knit(40);
		engine.endRow();
		// now right-side facing
		engine.startNewRow();
		engine.knit(10);
		engine.designateEndOfRow();
		engine.endRow();
	}

	@Test
	public void checkNeedleReturned() throws Exception {
		Needle newNeedle = knittingFactory.createNeedle("new-needle",
				NeedleStyle.CIRCULAR);
		List<Needle> needles = new ArrayList<Needle>();
		needles.add(newNeedle);
		engine.useNeedles(needles);
		engine.castOn(5);

		restore();
		// note that needles which were not in the engine
		// are not affected by the restore
		assertEquals(5, newNeedle.getTotalStitches());
	}

	@Test
	public void checkWorkingIntoNextStitchRestored() throws Exception {
		engine.startNewRow();
		engine.startWorkingIntoNextStitch();
		engine.knit();
		engine.purl();
		assertTrue(engine.isWorkingIntoStitch());
	}
	
}
