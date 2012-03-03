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

import org.junit.Before;
import org.junit.Test;

import com.knitml.core.common.NeedleStyle;
import com.knitml.core.model.operations.StitchNature;
import com.knitml.engine.Needle;
import com.knitml.engine.Stitch;
import com.knitml.engine.common.CannotPutMarkerOnEndOfNeedleException;
import com.knitml.engine.common.CannotWorkThroughMarkerException;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.common.NoMarkerFoundException;
import com.knitml.engine.common.NotEnoughStitchesException;
import com.knitml.engine.impl.DefaultKnittingFactory;
import com.knitml.engine.impl.DefaultMarker;
import com.knitml.engine.impl.DefaultNeedle;
import com.knitml.engine.impl.DefaultStitch;
import com.knitml.engine.settings.Direction;
import com.knitml.engine.settings.MarkerBehavior;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public abstract class FlatNeedleTests {

	protected Needle needle;

	protected void nextRow() {
		needle.turn();
	}

	protected void knit(int numberOfStitches) throws KnittingEngineException {
		for (int i = 0; i < numberOfStitches; i++) {
			needle.knit();
		}
	}

	protected void purl(int numberOfStitches) throws KnittingEngineException {
		for (int i = 0; i < numberOfStitches; i++) {
			needle.purl();
		}
	}

	protected void changeNeedleDirection() {
		if (needle.getDirection() == Direction.FORWARDS) {
			needle.setDirection(Direction.BACKWARDS);
		} else {
			needle.setDirection(Direction.FORWARDS);
		}
	}

	/**
	 * Cast 10 stitches onto a needle. Assert there are 10 stitches on that
	 * needle.
	 */
	@Before
	public final void setUp() throws Exception {
		initializeNeedle();
		needle.increase(10);
		assertEquals(10, needle.getTotalStitches());
		onSetUp();
	}

	protected void initializeNeedle() {
		needle = new DefaultNeedle("default", NeedleStyle.STRAIGHT, //$NON-NLS-1$
				new DefaultKnittingFactory());
	}

	protected abstract void onSetUp() throws KnittingEngineException;

	@Test
	public void peekAtNextStitch() throws Exception {
		List<Stitch> stitches = needle.getStitches();
		assertEquals(stitches.get(0), needle.peekAtNextStitch());
	}

	/**
	 * Test that the DefaultStitch collection cannot be modified.
	 * 
	 * @throws Exception
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void modifyStitches() throws Exception {
		List<Stitch> stitches = needle.getStitches();
		stitches.add(new DefaultStitch("blah")); //$NON-NLS-1$
	}

	@Test
	public void knitThree() throws Exception {
		knit(3);
		assertEquals(10, needle.getTotalStitches());
		assertEquals(7, needle.getStitchesRemaining());
	}

	@Test
	public void knitTwoTogether() throws Exception {
		needle.knit();
		needle.knitTwoTogether();
		assertEquals(9, needle.getTotalStitches());
		assertEquals(7, needle.getStitchesRemaining());
	}

	@Test
	public void knitThreeTogether() throws Exception {
		needle.knit();
		needle.knitThreeTogether();
		assertEquals(8, needle.getTotalStitches());
		assertEquals(6, needle.getStitchesRemaining());
	}

	@Test
	public void knitToEndOfRow() throws Exception {
		knit(10);
		assertEquals(10, needle.getTotalStitches());
		assertEquals(0, needle.getStitchesRemaining());
	}

	@Test(expected = NotEnoughStitchesException.class)
	public void knitTooFar() throws Exception {
		knit(11);
	}

	@Test
	public void purlThree() throws Exception {
		purl(3);
		assertEquals(10, needle.getTotalStitches());
		assertEquals(7, needle.getStitchesRemaining());
	}

	@Test
	public void purlTwoTogether() throws Exception {
		needle.purl();
		needle.purlTwoTogether();
		assertEquals(9, needle.getTotalStitches());
		assertEquals(7, needle.getStitchesRemaining());
	}

	@Test
	public void purlToMarker() throws Exception {
		purl(3);
		needle.placeMarker(new DefaultMarker());
		purl(7);
		assertEquals(0, needle.getStitchesRemaining());
		nextRow();
		// FIXME not right
	}

	@Test
	public void purlToEndOfRow() throws Exception {
		purl(10);
		assertEquals(10, needle.getTotalStitches());
		assertTrue(needle.isEndOfNeedle());
	}

	@Test
	public void decreaseAtEndOfNeedle() throws Exception {
		purl(8);
		needle.knitTwoTogether();
		assertEquals(9, needle.getTotalStitches());
		assertTrue(needle.isEndOfNeedle());
	}

	@Test(expected = NotEnoughStitchesException.class)
	public void decreaseOverEndOfNeedle() throws Exception {
		purl(9);
		needle.knitTwoTogether();
	}

	@Test(expected = NotEnoughStitchesException.class)
	public void purlTooFar() throws Exception {
		purl(11);
	}

	@Test
	public void misAssertEndOfNeedle() throws Exception {
		knit(9);
		assertEquals(1, needle.getStitchesRemaining());
		assertEquals(10, needle.getTotalStitches());
		assertFalse(needle.isEndOfNeedle());
	}

	@Test
	public void slipAroundTheNeedles() throws Exception {
		knit(2);
		needle.slip();
		assertEquals(7, needle.getStitchesRemaining());
		needle.reverseSlip();
		assertEquals(8, needle.getStitchesRemaining());
	}

	@Test
	public void slipAroundTheNeedlesOnTheWayBack() throws Exception {
		knitToEndOfRow();
		nextRow();
		purl(2);
		needle.slip();
		assertEquals(7, needle.getStitchesRemaining());
		needle.reverseSlip();
		assertEquals(8, needle.getStitchesRemaining());
	}

	@Test
	public void slipSlipKnit() throws Exception {
		needle.slip();
		needle.slip();
		needle.reverseSlip();
		needle.reverseSlip();
		needle.knitTwoTogether();
		assertEquals(8, needle.getStitchesRemaining());
	}

	@Test
	public void knitToMarker() throws Exception {
		knit(3);
		needle.placeMarker(new DefaultMarker());
		knit(7);

		nextRow();
		while (needle.getStitchesToNextMarker() > 0) {
			needle.purl();
		}
		assertEquals(3, needle.getStitchesRemaining());

	}

	@Test(expected = CannotWorkThroughMarkerException.class)
	public void knitTwoTogetherThroughMarker() throws Exception {
		knit(5);
		needle.placeMarker(new DefaultMarker());
		knit(5);

		nextRow();
		knit(4);
		needle.knitTwoTogether();
	}

	@Test
	public void knitTwoTogetherThroughMarkerAndPlaceMarkerAfterStitch()
			throws Exception {
		knit(5);
		needle.placeMarker(new DefaultMarker(
				MarkerBehavior.PLACE_AFTER_STITCH_WORKED));
		knit(5);

		nextRow();
		knit(4);
		needle.knitTwoTogether();
		assertEquals(0, needle.getStitchesToNextMarker());
	}

	@Test
	public void knitTwoTogetherThroughMarkerAndPlaceMarkerBeforeStitch()
			throws Exception {
		knit(5);
		needle.placeMarker(new DefaultMarker(
				MarkerBehavior.PLACE_BEFORE_STITCH_WORKED));
		knit(5);

		nextRow();
		knit(4);
		needle.knitTwoTogether();
		needle.turn();
		assertEquals(1, needle.getStitchesToNextMarker());
	}

	@Test
	public void knitTwoTogetherThroughMarkerAndRemoveMarker() throws Exception {
		knit(5);
		needle.placeMarker(new DefaultMarker(MarkerBehavior.REMOVE));
		knit(5);

		nextRow();
		knit(4);
		needle.knitTwoTogether();
		knit(4);

		nextRow();
		assertEquals(9, needle.getTotalStitches());
		assertFalse(needle.areMarkersRemaining());
	}

	@Test(expected = CannotWorkThroughMarkerException.class)
	public void knitThreeTogetherThroughMarker() throws Exception {
		knit(5);
		needle.placeMarker(new DefaultMarker());
		knit(5);

		nextRow();
		knit(4);
		needle.knitThreeTogether();
	}

	@Test(expected = CannotWorkThroughMarkerException.class)
	public void knitThreeTogetherThroughMarkerAgain() throws Exception {
		knit(5);
		needle.placeMarker(new DefaultMarker());
		knit(5);

		nextRow();
		knit(3);
		needle.knitThreeTogether();
	}

	@Test
	public void knitThreeTogetherThroughTwoMarkers() throws Exception {
		knit(3);
		needle.placeMarker(new DefaultMarker(MarkerBehavior.REMOVE));
		knit(1);
		needle.placeMarker(new DefaultMarker(MarkerBehavior.REMOVE));
		knit(6);

		// level set forwards and backwards directions since this test isn't
		// symmetric
		nextRow();
		knit(10);
		nextRow();

		knit(2);
		needle.knitThreeTogether();
		knit(5);
		nextRow();
		assertFalse(needle.areMarkersRemaining());
	}

	@Test
	public void knitThreeTogetherThroughMarkerAndPlaceMarkerAfterStitch()
			throws Exception {
		knit(5);
		needle.placeMarker(new DefaultMarker(
				MarkerBehavior.PLACE_AFTER_STITCH_WORKED));
		knit(5);

		nextRow();
		knit(3);
		needle.knitThreeTogether();
		assertEquals(0, needle.getStitchesToNextMarker());
	}

	@Test
	public void knitThreeTogetherThroughMarkerAndPlaceMarkerBeforeStitch()
			throws Exception {
		knit(5);
		needle.placeMarker(new DefaultMarker(
				MarkerBehavior.PLACE_BEFORE_STITCH_WORKED));
		knit(5);

		nextRow();
		knit(3);
		needle.knitThreeTogether();
		needle.turn();
		assertEquals(1, needle.getStitchesToNextMarker());
	}

	@Test
	public void knitThreeTogetherThroughMarkerAndRemoveMarker()
			throws Exception {
		knit(5);
		needle.placeMarker(new DefaultMarker(MarkerBehavior.REMOVE));
		knit(5);

		nextRow();
		knit(3);
		needle.knitThreeTogether();
		knit(4);

		nextRow();
		assertEquals(8, needle.getTotalStitches());
		assertFalse(needle.areMarkersRemaining());
	}

	@Test
	public void addAndRemoveMarker() throws Exception {
		knit(3);
		needle.placeMarker(new DefaultMarker());
		knit(7);

		nextRow();
		while (needle.getStitchesToNextMarker() > 0) {
			needle.knit();
		}
		assertEquals(3, needle.getStitchesRemaining());

		needle.removeMarker();
		knit(3);
		nextRow();
		assertFalse(needle.areMarkersRemaining());
	}

	@Test(expected = CannotPutMarkerOnEndOfNeedleException.class)
	public void placeMarkerAtEndOfNeedle() throws Exception {
		knit(10);
		needle.placeMarker(new DefaultMarker());
	}

	@Test(expected = CannotPutMarkerOnEndOfNeedleException.class)
	public void placeMarkerAtBeginningOfNeedle() throws Exception {
		needle.placeMarker(new DefaultMarker());
		knit(10);
	}
	
	@Test(expected = NoMarkerFoundException.class)
	public void getNonExistentMarker() throws Exception {
		needle.getStitchesToNextMarker();
	}

	@Test(expected = NoMarkerFoundException.class)
	public void removeNonExistentMarker() throws Exception {
		needle.removeMarker();
	}

	/**
	 * This test shows how to implement "knit to X before marker"
	 * 
	 * @throws Exception
	 */
	@Test
	public void knitToThreeBeforeMarker() throws Exception {
		// place marker between stitch 7 and 8
		knit(7);
		needle.placeMarker(new DefaultMarker());
		knit(3);

		// knit across row (this will neutralize round versus flat knitting)
		nextRow();
		knit(10);

		nextRow();
		while (needle.getStitchesToNextMarker() > 3) {
			needle.knit();
		}
		assertEquals(6, needle.getStitchesRemaining());

	}

	@Test
	public void knitToDifferentMarkers() throws Exception {
		knit(4);
		needle.placeMarker(new DefaultMarker());

		knit(5);
		needle.placeMarker(new DefaultMarker());
		needle.knit();

		// knit across row (this will neutralize round versus flat knitting)
		nextRow();
		knit(10);

		nextRow();
		while (needle.getStitchesToNextMarker() > 0) {
			needle.knit();
		}
		assertEquals(6, needle.getStitchesRemaining());
		// knit past the marker
		needle.knit();

		while (needle.getStitchesToNextMarker() > 0) {
			needle.knit();
		}
		assertEquals(1, needle.getStitchesRemaining());

	}

	@Test
	public void knitToDifferentMarkersWithDecreases() throws Exception {
		// place marker between stitch 2 and 3
		knit(2);
		needle.placeMarker(new DefaultMarker());

		// place marker between stitch 5 and 6
		knit(5);
		needle.placeMarker(new DefaultMarker());
		knit(3);

		// level the playing field
		nextRow();
		knit(10);

		nextRow();
		needle.knitTwoTogether();
		// slip marker
		needle.knitTwoTogether();
		needle.knit();
		needle.knitTwoTogether();
		// slip marker
		needle.knitTwoTogether();
		needle.knit();

		// level the playing field
		nextRow();
		knit(6);

		nextRow();
		needle.knit();
		assertEquals(0, needle.getStitchesToNextMarker());
		assertEquals(5, needle.getStitchesRemaining());
		knit(3);
		assertEquals(0, needle.getStitchesToNextMarker());
		assertEquals(2, needle.getStitchesRemaining());
		knit(2);
		assertEquals(0, needle.getStitchesRemaining());

	}

	@Test
	public void knitToDifferentMarkersWithDoubleDecreases() throws Exception {
		// place marker between stitch 3 and 4
		knit(3);
		needle.placeMarker(new DefaultMarker());

		// place marker between stitch 8 and 9
		knit(5);
		needle.placeMarker(new DefaultMarker());
		knit(2);

		// level the playing field
		nextRow();
		knit(10);

		nextRow();
		needle.knitThreeTogether();
		// slip marker
		needle.knitThreeTogether();
		needle.knitTwoTogether();
		// slip marker
		needle.knit();
		needle.knit();

		// level the playing field
		nextRow();
		knit(5);

		nextRow();
		needle.knit();
		assertEquals(0, needle.getStitchesToNextMarker());
		assertEquals(4, needle.getStitchesRemaining());
		knit(2);
		assertEquals(0, needle.getStitchesToNextMarker());
		assertEquals(2, needle.getStitchesRemaining());
		knit(2);
		assertEquals(0, needle.getStitchesRemaining());

	}

	@Test
	public void knitToDifferentMarkersWithIncreases() throws Exception {
		// place marker between stitch 2 and 3
		knit(2);
		needle.placeMarker(new DefaultMarker());
		knit(5);
		// place marker between stitch 7 and 8
		needle.placeMarker(new DefaultMarker());
		knit(3);

		// level the playing field
		nextRow();
		knit(10);

		nextRow();
		needle.increase(1);
		knit(2);
		// slip marker
		needle.increase(1);
		knit(3);
		needle.increase(1);
		knit(2);
		// slip marker
		needle.increase(1);
		knit(3);

		// level the playing field
		nextRow();
		knit(14);

		nextRow();
		knit(3);
		assertEquals(0, needle.getStitchesToNextMarker());
		assertEquals(11, needle.getStitchesRemaining());
		knit(7);
		assertEquals(0, needle.getStitchesToNextMarker());
		assertEquals(4, needle.getStitchesRemaining());
		knit(4);
		assertEquals(0, needle.getStitchesRemaining());

	}

	@Test
	public void knitToGap() throws Exception {
		knit(8);
		needle.turn();
		knit(8);
		needle.turn();
		assertEquals(8, needle.getStitchesToGap());
	}

	@Test
	public void knitOverGap() throws Exception {
		knit(8);
		needle.turn();
		knit(8);
		needle.turn();
		knit(10);
		nextRow();
		assertEquals(-1, needle.getStitchesToGap());
	}

	@Test
	public void turnASockHeel() throws Exception {
		knit(5);
		needle.knitTwoTogether();
		needle.turn();
		knit(1);
		needle.purlTwoTogether();
		needle.turn();
		knit(1);
		needle.knitTwoTogether();
		needle.turn();
		knit(1);
		needle.knitTwoTogether();
		needle.turn();
		assertEquals(6, needle.getTotalStitches());
		// when we turn the work, we're assuming that the gap we are actually
		// now sitting between does not count, since the other stitch that
		// participates in the gap is in the wrong direction
		assertEquals(2, needle.getStitchesToGap());
	}

	@Test(expected = IllegalArgumentException.class)
	public void placeNullMarker() throws Exception {
		knit(5);
		needle.placeMarker(null);
	}

	@Test(expected = NotEnoughStitchesException.class)
	public void reverseSlipOffTheNeedle() throws Exception {
		knit(1);
		needle.reverseSlip();
		needle.reverseSlip();
	}

	@Test
	public void bindOffAllStitchesOnNeedle() throws Exception {
		knit(1);
		int totalStitches = needle.getTotalStitches();
		for (int i = 0; i < totalStitches - 1; i++) {
			knit(1);
			needle.passPreviousStitchOver();
		}
		assertEquals(1, needle.getTotalStitches());
	}

	@Test
	public void bindOffAllStitchesOnNeedleThenFastenOff() throws Exception {
		knit(1);
		int totalStitches = needle.getTotalStitches();
		for (int i = 0; i < totalStitches - 1; i++) {
			knit(1);
			needle.passPreviousStitchOver();
		}
		needle.reverseSlip();
		needle.removeNextStitch();
		assertEquals(0, needle.getTotalStitches());
	}

	@Test(expected = NotEnoughStitchesException.class)
	public void bindOffStitchWhichIsNotThere() throws Exception {
		knit(1);
		needle.passPreviousStitchOver();
	}

	@Test
	public void crossStitches() throws Exception {
		knit(3);
		needle.cross(2, 3);
		knit(7);
		assertThat(needle.isEndOfNeedle(), is(true));

		List<Stitch> stitches = needle.getStitches();
		List<String> stitchNames = new ArrayList<String>(stitches.size());
		for (Stitch stitch : stitches) {
			stitchNames.add(stitch.getId());
		}
		String[] expectedStitchArray = new String[] { "A", "B", "C", "F", "G", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				"H", "D", "E", "I", "J" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		assertThat(stitchNames.toArray(new String[0]), is(expectedStitchArray));
	}

	@Test(expected = CannotWorkThroughMarkerException.class)
	public void crossStitchesOverMarker() throws Exception {
		knit(4);
		needle.placeMarker(new DefaultMarker());
		knit(6);
		nextRow();
		// get on the same page with subclasses
		knit(10);
		nextRow();

		// now perform the test
		knit(3);
		needle.cross(2, 3);
	}

	@Test
	public void crossStitchesNearMarker() throws Exception {
		knit(3);
		needle.placeMarker(new DefaultMarker());
		knit(5);
		needle.placeMarker(new DefaultMarker());
		knit(2);
		nextRow();
		// get on the same page with subclasses
		knit(10);
		nextRow();

		// now perform the test
		knit(3);
		needle.cross(2, 3);
		knit(7);
		assertThat(needle.isEndOfNeedle(), is(true));
	}

	@Test
	public void addStitchesToBeginning() throws Exception {
		List<Stitch> startingStitchesOnNeedle = new ArrayList<Stitch>(needle
				.getStitches());
		List<Stitch> stitchesToAdd = new ArrayList<Stitch>();
		Stitch firstStitch = new DefaultStitch("AA"); //$NON-NLS-1$
		stitchesToAdd.add(firstStitch);
		stitchesToAdd.add(new DefaultStitch("BB")); //$NON-NLS-1$
		stitchesToAdd.add(new DefaultStitch("CC")); //$NON-NLS-1$
		stitchesToAdd.add(new DefaultStitch("DD")); //$NON-NLS-1$

		// perform the operation on the needle
		needle.addStitchesToBeginning(stitchesToAdd);
		List<Stitch> expectedStitchesOnNeedle = new ArrayList<Stitch>();
		expectedStitchesOnNeedle.addAll(stitchesToAdd);
		expectedStitchesOnNeedle.addAll(startingStitchesOnNeedle);
		assertEquals(expectedStitchesOnNeedle, needle.getStitches());

		// make sure the stitch cursor is set to return the first stitch (i.e.
		// "AA")
		needle.startAtBeginning();
		assertEquals(firstStitch, needle.peekAtNextStitch());
	}

	@Test
	public void addStitchesToEnd() throws Exception {
		List<Stitch> startingStitchesOnNeedle = new ArrayList<Stitch>(needle
				.getStitches());
		List<Stitch> stitchesToAdd = new ArrayList<Stitch>();
		Stitch firstStitch = new DefaultStitch("K"); //$NON-NLS-1$
		stitchesToAdd.add(firstStitch);
		stitchesToAdd.add(new DefaultStitch("L")); //$NON-NLS-1$
		stitchesToAdd.add(new DefaultStitch("M")); //$NON-NLS-1$
		stitchesToAdd.add(new DefaultStitch("N")); //$NON-NLS-1$

		// perform the operation on the needle
		needle.addStitchesToEnd(stitchesToAdd);
		List<Stitch> expectedStitchesOnNeedle = new ArrayList<Stitch>();
		expectedStitchesOnNeedle.addAll(startingStitchesOnNeedle);
		expectedStitchesOnNeedle.addAll(stitchesToAdd);
		assertEquals(expectedStitchesOnNeedle, needle.getStitches());

		// make sure the stitch cursor is set to return the first stitch (i.e.
		// "AA")
		needle.startAtBeginning();
		knit(10);
		assertEquals(firstStitch, needle.peekAtNextStitch());
	}

	@Test
	public void verifyLastOperation() throws Exception {
		knit(10);
		nextRow();
		while (needle.getStitchesRemaining() > 0) {
			assertThat(needle.peekAtNextStitch().getCurrentNature(), is (StitchNature.KNIT));
			needle.knit();
		}
	}

	@Test
	public void verifyLastOperationAcrossMultipleRows() throws Exception {
		// forward side
		knit(10);
		nextRow();
		// backward side: knitting here means that a purl will be recorded for the StitchOperation
		knit(10);
		nextRow();
		// forward side 
		while (needle.getStitchesRemaining() > 0) {
			assertThat(needle.peekAtNextStitch().getCurrentNature(), is (StitchNature.PURL));
			needle.knit();
		}
	}
	
}
