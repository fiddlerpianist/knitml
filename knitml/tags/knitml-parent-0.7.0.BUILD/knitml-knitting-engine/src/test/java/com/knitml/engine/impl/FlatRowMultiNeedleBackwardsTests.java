/**
 * 
 */
package com.knitml.engine.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.knitml.core.common.NeedleStyle;
import com.knitml.engine.Needle;
import com.knitml.engine.Stitch;


/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class FlatRowMultiNeedleBackwardsTests extends FlatRowMultiNeedleTests {

	@Override
	public void onSetUp() throws Exception {
		engine.startNewRow();
	}
	
	@Override
	@Ignore
	public void getStitchesRemainingOnCurrentNeedle() throws Exception {
		// not a valid test
	}

	@Override
	@Ignore
	public void transferStitchesToPreviousNeedle() throws Exception {
		// not a valid test
	}
	
	@Override
	@Ignore
	public void getTotalNumberOfStitchesOnCurrentNeedle() throws Exception {
		// not a valid test
	}

	@Override
	@Ignore
	public void k2togBetweenNeedles() throws Exception {
		// not a valid test
	}

	@Override
	@Ignore
	public void p2togBetweenNeedles() throws Exception {
		// not a valid test
	}

	@Override
	@Ignore
	public void k3togBetweenNeedles() throws Exception {
		// not a valid test
	}
	
	@Override
	@Test
	public void transferStitchesToSpareNeedle() throws Exception {
		Needle stitchHolder = knittingFactory.createNeedle("stitch-holder", NeedleStyle.CIRCULAR); //$NON-NLS-1$
		knit(18);
		engine.transferStitchesToNeedle(stitchHolder, 4);
		knit(18);
		engine.startNewRow();
		knit(36);
		engine.startNewRow();
		
		assertThat(engine.getTotalNumberOfStitchesOnCurrentNeedle(), is (18));

		List<Stitch> firstNeedleStitches = engine.getCurrentNeedle().getStitches();
		assertThat(firstNeedleStitches.get(15).getId(), is ("Y")); //$NON-NLS-1$
		assertThat(firstNeedleStitches.get(16).getId(), is ("X")); //$NON-NLS-1$
		assertThat(firstNeedleStitches.get(17).getId(), is ("W")); //$NON-NLS-1$
		
		List<Stitch> stitchHolderStitches = stitchHolder.getStitches();
		assertThat(stitchHolderStitches.get(0).getId(), is ("V")); //$NON-NLS-1$
		assertThat(stitchHolderStitches.get(1).getId(), is ("U")); //$NON-NLS-1$
		assertThat(stitchHolderStitches.get(2).getId(), is ("T")); //$NON-NLS-1$
		assertThat(stitchHolderStitches.get(3).getId(), is ("S")); //$NON-NLS-1$
		
		// go to the second needle
		knit(20);
		List<Stitch> secondNeedleStitches = engine.getCurrentNeedle().getStitches();
		assertThat(secondNeedleStitches.get(0).getId(), is ("R")); //$NON-NLS-1$
		assertThat(secondNeedleStitches.get(1).getId(), is ("Q")); //$NON-NLS-1$
		
	}

	
}
