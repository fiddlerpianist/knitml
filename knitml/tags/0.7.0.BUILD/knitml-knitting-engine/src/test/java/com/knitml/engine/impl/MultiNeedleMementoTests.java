/**
 * 
 */
package com.knitml.engine.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.NeedleStyle;
import com.knitml.engine.KnittingFactory;
import com.knitml.engine.Needle;
import com.knitml.engine.settings.Direction;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class MultiNeedleMementoTests {

	protected DefaultKnittingEngine engine;
	protected KnittingFactory knittingFactory = new DefaultKnittingFactory();
	protected Needle needle1 = knittingFactory.createNeedle("needle1", //$NON-NLS-1$
			NeedleStyle.CIRCULAR);
	protected Needle needle2 = knittingFactory.createNeedle("needle2", //$NON-NLS-1$
			NeedleStyle.CIRCULAR);
	protected Needle needle3 = knittingFactory.createNeedle("needle3", //$NON-NLS-1$
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
		needles.add(needle1);
		engine.useNeedles(needles);
		engine.castOn(40, true);
		needles.add(needle2);
		needles.add(needle3);
		engine.useNeedles(needles);
		engine.declareRoundKnitting();
		engine.arrangeStitchesOnNeedles(new int[] { 5, 15, 20 });
		assertOriginalState();
		save();
	}
	
	@After
	public final void verify() throws Exception {
		restore();
		assertOriginalState();
		checkNextRowAfterInitial();
	}
	
	private void assertOriginalState() {
		// capture state right after the cast-on row before startNewRow() is
		// called
		assertEquals(1, engine.getTotalRowsCompleted());
		assertEquals(1, engine.getCurrentRowNumber());
		assertEquals(40, engine.getTotalNumberOfStitchesInRow());
		assertEquals(Direction.FORWARDS, engine.getDirection());
		assertEquals(KnittingShape.ROUND, engine.getKnittingShape());
		assertEquals(3, engine.getNumberOfNeedles());
	}

	private void checkNextRowAfterInitial() throws Exception {
		engine.startNewRow();
		assertEquals(40, engine.getStitchesRemainingInRow());
		assertEquals(1, engine.getTotalRowsCompleted());
		assertEquals(2, engine.getCurrentRowNumber());
		assertEquals(needle1, engine.getCurrentNeedle());
		engine.knit(5);
		engine.advanceNeedle();
		assertEquals(needle2, engine.getCurrentNeedle());
		engine.knit(15);
		engine.advanceNeedle();
		assertEquals(needle3, engine.getCurrentNeedle());
		engine.knit(20);
		engine.endRow();
	}

	@Test
	public void rearrangeStitchesOnNeedles() throws Exception {
		engine.arrangeStitchesOnNeedles(new int[] { 20, 15, 5 });
		engine.startNewRow();
		assertEquals(20, engine.getStitchesRemainingOnCurrentNeedle());
	}

	@Test
	public void decreaseOnFirstNeedleThenRemoveItFromEngine() throws Exception {
		engine.startNewRow();
		engine.knitTwoTogether();
		engine.knit(38);
		engine.endRow();
		List<Needle> needles = new ArrayList<Needle>();
		needles.add(needle2);
		needles.add(needle3);
		engine.useNeedles(needles);
		engine.arrangeStitchesOnNeedles(new int[] { 17, 18 });
	}

	@Test
	public void addNeedleAfterSave() throws Exception {
		List<Needle> needles = new ArrayList<Needle>();
		needles.add(needle1);
		needles.add(needle2);
		needles.add(needle3);
		Needle needle4 = knittingFactory.createNeedle("needle4", NeedleStyle.CIRCULAR); //$NON-NLS-1$
		Object needle4State = needle4.save();
		needles.add(needle4);
		engine.useNeedles(needles);
		engine.arrangeStitchesOnNeedles(new int[] { 10, 10, 10, 10});
		engine.startNewRow();
		engine.knit(38);
		// decrease 1 stitch on needle4
		engine.knitTwoTogether();
		engine.endRow();
		restore();
		assertEquals(9, needle4.getTotalStitches());
		// Note that needles added AFTER a save are NOT restored to their original state.
		// They would have to get their own memento created, like this: 
		needle4.restore(needle4State);
		assertEquals(0, needle4.getTotalStitches());
	}
	
}
