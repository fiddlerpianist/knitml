/**
 * 
 */
package com.knitml.engine.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.knitml.engine.impl.DefaultKnittingEngine;
import com.knitml.engine.impl.DefaultKnittingFactory;
import com.knitml.engine.settings.Direction;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class FlatRowCastOnTests {

	protected DefaultKnittingEngine knitter;

	@Before
	public void setUp() throws Exception {
		knitter = new DefaultKnittingEngine(new DefaultKnittingFactory());
	}
	
	@Test
	public void noRowCount() throws Exception {
		knitter.castOn(40, false);
		knitter.startNewRow();
		assertTrue(knitter.getDirection() == Direction.FORWARDS);
	}


}
