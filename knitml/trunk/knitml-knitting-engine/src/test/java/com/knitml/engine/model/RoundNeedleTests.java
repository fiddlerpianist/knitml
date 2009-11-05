/**
 * 
 */
package com.knitml.engine.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.knitml.core.common.NeedleStyle;
import com.knitml.core.model.directions.StitchNature;
import com.knitml.engine.Stitch;
import com.knitml.engine.impl.DefaultKnittingFactory;
import com.knitml.engine.impl.DefaultMarker;
import com.knitml.engine.impl.DefaultNeedle;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class RoundNeedleTests extends FlatNeedleTests {

	@Override
	public void onSetUp() {
		needle.startAtBeginning();
	}
	
	@Override
	protected void initializeNeedle() {
		needle = new DefaultNeedle("default", NeedleStyle.CIRCULAR, new DefaultKnittingFactory());
	}

	@Override
	protected void nextRow() {
		needle.startAtBeginning();
	}

	@Test
	public void knitForTwoRounds() throws Exception {
		knit(10);
		nextRow();
		knit(10);
		nextRow();
	}
	
	@Test
	@Override
	public void knitToMarker() throws Exception {
		knit(3);
		needle.placeMarker(new DefaultMarker());
		knit(7);
		assertTrue(needle.isEndOfNeedle());
		
		// knit 3, 7 stitches should be left
		nextRow();
		while (needle.getStitchesToNextMarker() > 0) {
			needle.knit();
		}
		assertEquals(7, needle.getStitchesRemaining());
	}
	
	@Test
	public void removeNStitchesFromBeginning() throws Exception {
		List<Stitch> startingStitchesOnNeedle = new ArrayList<Stitch>(needle.getStitches());
		needle.removeNStitchesFromBeginning(4);
		assertEquals(startingStitchesOnNeedle.subList(4, 10), needle.getStitches());
	}	

	@Test
	public void removeNStitchesFromEnd() throws Exception {
		List<Stitch> startingStitchesOnNeedle = new ArrayList<Stitch>(needle.getStitches());
		needle.removeNStitchesFromEnd(4);
		assertEquals(startingStitchesOnNeedle.subList(0, 6), needle.getStitches());
	}	

	@Test
	@Override
	public void addAndRemoveMarker() throws Exception {
		knit(3);
		needle.placeMarker(new DefaultMarker());
		knit(7);

		nextRow();
		while (needle.getStitchesToNextMarker() > 0) {
			needle.knit();
		}
		assertEquals(7, needle.getStitchesRemaining());
		
		needle.removeMarker();
		knit(7);
		nextRow();
		assertFalse(needle.areMarkersRemaining());
	}

	@Test
	@Override
	public void verifyLastOperationAcrossMultipleRows() throws Exception {
		// forward side
		knit(10);
		nextRow();
		// forward side again
		knit(10);
		nextRow();
		// forward side 
		while (needle.getStitchesRemaining() > 0) {
			assertThat(needle.peekAtNextStitch().getCurrentNature(), is (StitchNature.KNIT));
			needle.knit();
		}
	}
	
}
