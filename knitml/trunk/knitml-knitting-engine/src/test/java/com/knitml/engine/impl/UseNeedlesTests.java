/**
 * 
 */
package com.knitml.engine.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.junit.Before;
import org.junit.Test;

import com.knitml.core.common.NeedleStyle;
import com.knitml.engine.KnittingEngine;
import com.knitml.engine.KnittingFactory;
import com.knitml.engine.Needle;
import com.knitml.engine.common.NotBetweenRowsException;
import com.knitml.engine.common.WrongNeedleTypeException;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class UseNeedlesTests {

	protected KnittingEngine knitter;
	protected KnittingFactory knittingFactory;

	@Before
	public final void setUp() throws Exception {
		knittingFactory = new DefaultKnittingFactory();
		knitter = new DefaultKnittingEngine(knittingFactory);
	}

	@Test(expected = WrongNeedleTypeException.class)
	public void useAllStraightNeedles() throws Exception {
		List<Needle> needles = new ArrayList<Needle>();
		needles.add(knittingFactory.createNeedle("needle1", NeedleStyle.STRAIGHT));
		needles.add(knittingFactory.createNeedle("needle2", NeedleStyle.STRAIGHT));
		
		knitter.useNeedles(needles);
	}

	@Test(expected = WrongNeedleTypeException.class)
	public void useNeedleMixture() throws Exception {
		List<Needle> needles = new ArrayList<Needle>();
		needles.add(knittingFactory.createNeedle("needle1", NeedleStyle.STRAIGHT));
		needles.add(knittingFactory.createNeedle("needle2", NeedleStyle.DPN));
		knitter.useNeedles(needles);
	}

	@Test(expected = WrongNeedleTypeException.class)
	public void useStraightNeedleForRoundKnitting() throws Exception {
		List<Needle> needles = new ArrayList<Needle>();
		needles.add(knittingFactory.createNeedle("needle1", NeedleStyle.STRAIGHT));
		knitter.useNeedles(needles);
		knitter.declareRoundKnitting();
	}

	@Test(expected = WrongNeedleTypeException.class)
	public void useDpnForRoundKnitting() throws Exception {
		List<Needle> needles = new ArrayList<Needle>();
		needles.add(knittingFactory.createNeedle("needle1", NeedleStyle.DPN));
		knitter.useNeedles(needles);
		knitter.declareRoundKnitting();
	}

	@Test(expected = WrongNeedleTypeException.class)
	public void useStraightNeedlesForRoundKnittingDeclareFirst()
			throws Exception {
		List<Needle> needles = new ArrayList<Needle>();
		knitter.declareRoundKnitting();
		needles.add(knittingFactory.createNeedle("needle1", NeedleStyle.STRAIGHT));
		knitter.useNeedles(needles);
	}

	@Test
	public void useAllDoublePointedNeedles() throws Exception {
		List<Needle> needles = new ArrayList<Needle>();
		needles.add(knittingFactory.createNeedle("needle1", NeedleStyle.DPN));
		needles.add(knittingFactory.createNeedle("needle2", NeedleStyle.DPN));
		needles.add(knittingFactory.createNeedle("needle3", NeedleStyle.DPN));
		knitter.useNeedles(needles);
	}

	@Test
	public void declareRoundKnittingWithDPNs() throws Exception {
		useAllDoublePointedNeedles();
		knitter.declareRoundKnitting();
	}

	@Test
	public void declareRoundKnittingWithDPNsDeclareFirst() throws Exception {
		knitter.declareRoundKnitting();
		useAllDoublePointedNeedles();
	}

	@Test
	public void useAllCircularNeedles() throws Exception {
		List<Needle> needles = new ArrayList<Needle>();
		needles.add(knittingFactory.createNeedle("needle1", NeedleStyle.CIRCULAR));
		needles.add(knittingFactory.createNeedle("needle2", NeedleStyle.CIRCULAR));
		knitter.useNeedles(needles);
	}

	@Test
	public void useOnlyOneCircularNeedle() throws Exception {
		List<Needle> needles = new ArrayList<Needle>();
		needles.add(knittingFactory.createNeedle("needle1", NeedleStyle.CIRCULAR));
		knitter.useNeedles(needles);
	}

	@Test
	public void declareRoundKnittingWithCircs() throws Exception {
		useAllCircularNeedles();
		knitter.declareRoundKnitting();
	}

	@Test
	public void declareRoundKnittingFirstWithCircs() throws Exception {
		knitter.declareRoundKnitting();
		useAllCircularNeedles();
	}

	@Test
	public void declareRoundKnittingWithOneCirc() throws Exception {
		useOnlyOneCircularNeedle();
		knitter.declareRoundKnitting();
	}

	@Test
	public void declareRoundKnittingFirstWithOneCirc() throws Exception {
		knitter.declareRoundKnitting();
		useOnlyOneCircularNeedle();
	}

	@Test(expected = NullArgumentException.class)
	public void passNullNeedleList() throws Exception {
		knitter.useNeedles(null);
	}

	@Test(expected = NullArgumentException.class)
	public void passNullNeedle() throws Exception {
		List<Needle> needles = new ArrayList<Needle>();
		needles.add(null);
		knitter.useNeedles(needles);
	}

	@Test(expected = NotBetweenRowsException.class)
	public void useNeedlesInRow() throws Exception {
		knitter.castOn(10);
		knitter.startNewRow();
		knitter.knit();
		List<Needle> needlesToUse = new ArrayList<Needle>();
		needlesToUse.add(knittingFactory.createNeedle("needle1", NeedleStyle.CIRCULAR));
		knitter.useNeedles(needlesToUse);
	}
	
	@Test(expected = NotBetweenRowsException.class)
	public void useNeedlesWhileWorkingBackwards() throws Exception {
		knitter.castOn(1);
		knitter.startNewRow();
		List<Needle> needlesToUse = new ArrayList<Needle>();
		needlesToUse.add(knittingFactory.createNeedle("needle1", NeedleStyle.CIRCULAR));
		knitter.useNeedles(needlesToUse);
	}
	
}
