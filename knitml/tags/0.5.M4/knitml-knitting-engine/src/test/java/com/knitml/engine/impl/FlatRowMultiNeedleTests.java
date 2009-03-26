/**
 * 
 */
package com.knitml.engine.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.knitml.core.common.NeedleStyle;
import com.knitml.core.model.directions.inline.Increase;
import com.knitml.engine.KnittingFactory;
import com.knitml.engine.Needle;
import com.knitml.engine.impl.DefaultKnittingEngine;
import com.knitml.engine.impl.DefaultKnittingFactory;
import com.knitml.engine.settings.Direction;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public abstract class FlatRowMultiNeedleTests extends FlatRowTests {

	@SuppressWarnings("unused")
	private final static String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	@Override
	public void setUp() throws Exception {
		KnittingFactory knittingFactory = new DefaultKnittingFactory();
		knitter = new DefaultKnittingEngine(knittingFactory);
		Needle needle1 = knittingFactory.createNeedle("needle1",
				NeedleStyle.DPN);
		List<Needle> needlesToUse = new ArrayList<Needle>();
		needlesToUse.add(needle1);
		knitter.useNeedles(needlesToUse);
		knitter.castOn(40, true);
		needlesToUse.add(knittingFactory.createNeedle("needle2",
				NeedleStyle.DPN));
		needlesToUse.add(knittingFactory.createNeedle("needle3",
				NeedleStyle.DPN));
		knitter.useNeedles(needlesToUse);
		knitter.arrangeStitchesOnNeedles(new int[] { 10, 10, 20 });
		knitter.resetRowNumber();
		onSetUp();
	}

	@Test
	public void transferStitchesToPreviousNeedle() throws Exception {
		knitter.knit(40);
		knitter.startNewRow();
		knitter.knit(40);
		knitter.endRow();
		knitter.arrangeStitchesOnNeedles(new int[] { 20, 5, 15 });
		knitter.startNewRow();
		assertEquals(Direction.FORWARDS, knitter.getDirection());
		assertEquals(20, knitter.getTotalNumberOfStitchesOnCurrentNeedle());
		knit(20);
		// haven't switched over to the next needle yet
		assertEquals(20, knitter.getTotalNumberOfStitchesOnCurrentNeedle());
		knit(5);
		// haven't switched over to the next needle yet
		assertEquals(5, knitter.getTotalNumberOfStitchesOnCurrentNeedle());
		knit(15);
		assertEquals(15, knitter.getTotalNumberOfStitchesOnCurrentNeedle());
		knitter.endRow();
	}

	@Test
	@Override
	public void getStitchesRemainingOnCurrentNeedle() throws Exception {
		knit(5);
		assertEquals(5, knitter.getStitchesRemainingOnCurrentNeedle());
		knit(5);
		assertEquals(0, knitter.getStitchesRemainingOnCurrentNeedle());
		knit(5);
		assertEquals(5, knitter.getStitchesRemainingOnCurrentNeedle());
		knit(5);
		assertEquals(0, knitter.getStitchesRemainingOnCurrentNeedle());
		knit(5);
		assertEquals(15, knitter.getStitchesRemainingOnCurrentNeedle());
		knit(15);
		assertEquals(0, knitter.getStitchesRemainingOnCurrentNeedle());
		knitter.endRow();
	}

	@Test
	@Override
	public void getTotalNumberOfStitchesOnCurrentNeedle() throws Exception {
		assertEquals(10, knitter.getTotalNumberOfStitchesOnCurrentNeedle());
		knit(10);
		// haven't switched over to the next needle yet
		assertEquals(10, knitter.getTotalNumberOfStitchesOnCurrentNeedle());
		knit(5);
		for (int i=0; i < 5; i++) {
			knitter.increase(new Increase());
		}
		assertEquals(15, knitter.getTotalNumberOfStitchesOnCurrentNeedle());
		knit(6);
		assertEquals(20, knitter.getTotalNumberOfStitchesOnCurrentNeedle());
		knit(19);
		knitter.endRow();
	}

}
