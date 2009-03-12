/**
 * 
 */
package com.knitml.engine.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.knitml.core.common.NeedleStyle;
import com.knitml.engine.Stitch;
import com.knitml.engine.impl.DefaultKnittingFactory;
import com.knitml.engine.impl.DefaultMarker;
import com.knitml.engine.impl.DefaultNeedle;
import com.knitml.engine.impl.DefaultStitch;

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
	public void addStitchesToBeginning() throws Exception {
		List<Stitch> startingStitchesOnNeedle = new ArrayList<Stitch>(needle.getStitches());
		List<Stitch> stitchesToAdd = new ArrayList<Stitch>();
		Stitch firstStitch = new DefaultStitch("AA");
		stitchesToAdd.add(firstStitch);
		stitchesToAdd.add(new DefaultStitch("BB"));
		stitchesToAdd.add(new DefaultStitch("CC"));
		stitchesToAdd.add(new DefaultStitch("DD"));
		
		// perform the operation on the needle
		needle.addStitchesToBeginning(stitchesToAdd);
		List<Stitch> expectedStitchesOnNeedle = new ArrayList<Stitch>();
		expectedStitchesOnNeedle.addAll(stitchesToAdd);
		expectedStitchesOnNeedle.addAll(startingStitchesOnNeedle);
		assertEquals(expectedStitchesOnNeedle, needle.getStitches());
		
		// make sure the stitch cursor is set to return the first stitch (i.e. "AA")
		needle.startAtBeginning();
		assertEquals(firstStitch, needle.peekAtNextStitch());
	}	

	@Test
	public void addStitchesToEnd() throws Exception {
		List<Stitch> startingStitchesOnNeedle = new ArrayList<Stitch>(needle.getStitches());
		List<Stitch> stitchesToAdd = new ArrayList<Stitch>();
		Stitch firstStitch = new DefaultStitch("K");
		stitchesToAdd.add(firstStitch);
		stitchesToAdd.add(new DefaultStitch("L"));
		stitchesToAdd.add(new DefaultStitch("M"));
		stitchesToAdd.add(new DefaultStitch("N"));
		
		// perform the operation on the needle
		needle.addStitchesToEnd(stitchesToAdd);
		List<Stitch> expectedStitchesOnNeedle = new ArrayList<Stitch>();
		expectedStitchesOnNeedle.addAll(startingStitchesOnNeedle);
		expectedStitchesOnNeedle.addAll(stitchesToAdd);
		assertEquals(expectedStitchesOnNeedle, needle.getStitches());
		
		// make sure the stitch cursor is set to return the first stitch (i.e. "AA")
		needle.startAtBeginning();
		knit(10);
		assertEquals(firstStitch, needle.peekAtNextStitch());
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
	
}
