/**
 * 
 */
package com.knitml.engine.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.knitml.core.common.NeedleStyle;
import com.knitml.engine.KnittingEngine;
import com.knitml.engine.KnittingFactory;
import com.knitml.engine.Needle;
import com.knitml.engine.Stitch;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.common.WrongNumberOfNeedlesException;
import com.knitml.engine.impl.DefaultKnittingEngine;
import com.knitml.engine.impl.DefaultKnittingFactory;
import com.knitml.engine.impl.DefaultStitch;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class ArrangeStitchesOnNeedlesTests {

	protected KnittingEngine knitter;
	protected KnittingFactory knittingFactory = new DefaultKnittingFactory();
	protected Needle initialNeedle = knittingFactory.createNeedle("default",
			NeedleStyle.CIRCULAR);
	private static List<Stitch> expectedStitchOrder = new ArrayList<Stitch>(40);

	protected void knit(int numberOfStitches) throws KnittingEngineException {
		for (int i = 0; i < numberOfStitches; i++) {
			knitter.knit();
		}
	}
	
	@BeforeClass
	public static void setUpExpectedStitchOrder() {
		String stitchesString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmn";
		for (int i=0; i < 40; i++) {
			expectedStitchOrder.add(new DefaultStitch(String.valueOf(stitchesString.charAt(i))));
		}
	}
	

	@Before
	public final void setUp() throws Exception {
		knitter = new DefaultKnittingEngine(knittingFactory);
		List<Needle> needles = new ArrayList<Needle>();
		needles.add(initialNeedle);
		knitter.useNeedles(needles);
		knitter.castOn(40, true);
	}

	@Test
	public void moveStitchesBackwards() throws Exception {
		List<Needle> newNeedles = new ArrayList<Needle>();
		newNeedles.add(knittingFactory.createNeedle("needle1", NeedleStyle.CIRCULAR));
		newNeedles.add(knittingFactory.createNeedle("needle2", NeedleStyle.CIRCULAR));
		newNeedles.add(initialNeedle);
		knitter.useNeedles(newNeedles);
		knitter.arrangeStitchesOnNeedles(new int[] { 10, 10, 20 });
		verifyStitchOrder(newNeedles);
		assertStitchesOnNeedle(10, 10, 20);
	}

	@Test
	public void moveStitchesBackwardsAgain() throws Exception {
		List<Needle> newNeedles = new ArrayList<Needle>();
		Needle needle2 = knittingFactory.createNeedle("needle2", NeedleStyle.CIRCULAR); 
		newNeedles.add(needle2);
		newNeedles.add(initialNeedle);
		knitter.useNeedles(newNeedles);
		newNeedles.clear();
		knitter.arrangeStitchesOnNeedles(new int[] { 5, 35 });
		Needle needle1 = knittingFactory.createNeedle("needle1", NeedleStyle.CIRCULAR); 
		newNeedles.add(needle1);
		newNeedles.add(needle2);
		newNeedles.add(initialNeedle);
		knitter.useNeedles(newNeedles);
		knitter.arrangeStitchesOnNeedles(new int[] { 10, 10, 20 });
		verifyStitchOrder(newNeedles);
		assertStitchesOnNeedle(10, 10, 20);
	}
	
	@Test(expected=WrongNumberOfNeedlesException.class)
	public void rearrangeToNotEnoughNeedles() throws Exception {
		List<Needle> newNeedles = new ArrayList<Needle>();
		newNeedles.add(knittingFactory.createNeedle("needle1", NeedleStyle.CIRCULAR));
		newNeedles.add(knittingFactory.createNeedle("needle2", NeedleStyle.CIRCULAR));
		newNeedles.add(initialNeedle);
		knitter.useNeedles(newNeedles);
		knitter.arrangeStitchesOnNeedles(new int[] { 20, 20 });
	}
	
	@Test
	public void moveStitchesForwards() throws Exception {
		List<Needle> newNeedles = new ArrayList<Needle>();
		newNeedles.add(initialNeedle);
		newNeedles.add(knittingFactory.createNeedle("needle2", NeedleStyle.CIRCULAR));
		newNeedles.add(knittingFactory.createNeedle("needle3", NeedleStyle.CIRCULAR));
		knitter.useNeedles(newNeedles);
		knitter.arrangeStitchesOnNeedles(new int[] { 10, 10, 20 });
		verifyStitchOrder(newNeedles);
		assertStitchesOnNeedle(10, 10, 20);
	}
	
	@Test
	public void moveStitchesForwardsAgain() throws Exception {
		List<Needle> newNeedles = new ArrayList<Needle>();
		newNeedles.add(initialNeedle);
		newNeedles.add(knittingFactory.createNeedle("needle2", NeedleStyle.CIRCULAR));
		newNeedles.add(knittingFactory.createNeedle("needle3", NeedleStyle.CIRCULAR));
		knitter.useNeedles(newNeedles);
		knitter.arrangeStitchesOnNeedles(new int[] { 1, 1, 38 });
		verifyStitchOrder(newNeedles);
		assertStitchesOnNeedle(1, 1, 38);
	}

	@Test
	public void moveStitchesToMiddle() throws Exception {
		List<Needle> newNeedles = new ArrayList<Needle>();
		newNeedles.add(initialNeedle);
		newNeedles.add(knittingFactory.createNeedle("needle2", NeedleStyle.CIRCULAR));
		newNeedles.add(knittingFactory.createNeedle("needle3", NeedleStyle.CIRCULAR));
		knitter.useNeedles(newNeedles);
		knitter.arrangeStitchesOnNeedles(new int[] { 1, 38, 1 });
		verifyStitchOrder(newNeedles);
		assertStitchesOnNeedle(1, 38, 1);
	}
	
	@Test
	public void moveStitchesFromMiddle() throws Exception {
		List<Needle> newNeedles = new ArrayList<Needle>();
		newNeedles.add(knittingFactory.createNeedle("needle1", NeedleStyle.CIRCULAR));
		newNeedles.add(initialNeedle);
		newNeedles.add(knittingFactory.createNeedle("needle3", NeedleStyle.CIRCULAR));
		knitter.useNeedles(newNeedles);
		knitter.arrangeStitchesOnNeedles(new int[] { 12, 10, 18 });
		verifyStitchOrder(newNeedles);
		assertStitchesOnNeedle(12, 10, 18);
	}

	private void assertStitchesOnNeedle(int i, int j, int k) throws Exception {
		knitter.startNewRow();
		knit(40);
		knitter.startNewRow();
		assertEquals(i, knitter.getStitchesRemainingOnCurrentNeedle());
		knit(i);
		knitter.advanceNeedle();
		assertEquals(j, knitter.getStitchesRemainingOnCurrentNeedle());
		knit(j);
		knitter.advanceNeedle();
		assertEquals(k, knitter.getStitchesRemainingOnCurrentNeedle());
		knit(k);
		knitter.endRow();
	}
	
	public void verifyStitchOrder(List<Needle> needles) throws Exception {
		List<Stitch> allStitches = new ArrayList<Stitch>(40);
		for (Needle needle : needles) {
			allStitches.addAll(needle.getStitches());
		}
		assertEquals(expectedStitchOrder, allStitches);
	}
	
}
