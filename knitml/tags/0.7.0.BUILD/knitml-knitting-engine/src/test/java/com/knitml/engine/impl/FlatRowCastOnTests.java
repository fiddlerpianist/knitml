/**
 * 
 */
package com.knitml.engine.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.knitml.engine.settings.Direction;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class FlatRowCastOnTests {

	protected DefaultKnittingEngine engine;

	@Before
	public void setUp() throws Exception {
		engine = new DefaultKnittingEngine(new DefaultKnittingFactory());
	}
	
	@Test
	public void noRowCount() throws Exception {
		engine.castOn(40, false);
		engine.startNewRow();
		assertThat (engine.getDirection(), is (Direction.FORWARDS));
	}

}
