/**
 * 
 */
package com.knitml.engine.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.knitml.core.common.NeedleStyle;
import com.knitml.core.model.operations.inline.Increase;
import com.knitml.engine.KnittingFactory;
import com.knitml.engine.Needle;
import com.knitml.engine.Stitch;
import com.knitml.engine.settings.Direction;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public abstract class FlatRowMultiNeedleTests extends FlatRowTests {

	@SuppressWarnings("unused")
	private final static String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; //$NON-NLS-1$
	protected KnittingFactory knittingFactory;

	@Override
	public void setUp() throws Exception {
		knittingFactory = new DefaultKnittingFactory();
		engine = new DefaultKnittingEngine(knittingFactory);
		Needle needle1 = knittingFactory.createNeedle("needle1", //$NON-NLS-1$
				NeedleStyle.DPN);
		List<Needle> needlesToUse = new ArrayList<Needle>();
		needlesToUse.add(needle1);
		engine.useNeedles(needlesToUse);
		engine.castOn(40, true);
		needlesToUse.add(knittingFactory.createNeedle("needle2", //$NON-NLS-1$
				NeedleStyle.DPN));
		needlesToUse.add(knittingFactory.createNeedle("needle3", //$NON-NLS-1$
				NeedleStyle.DPN));
		engine.useNeedles(needlesToUse);
		engine.arrangeStitchesOnNeedles(new int[] { 10, 10, 20 });
		engine.resetRowNumber();
		onSetUp();
	}

	@Test
	public void k2togBetweenNeedles() throws Exception {
		engine.knit(9);
		engine.knitTwoTogether();
		engine.knit(8);
		engine.knitTwoTogether();
		engine.knit(19);
		engine.endRow();
		
		// neutralizes flat vs. round
		engine.startNewRow();
		engine.knit(38);
		engine.endRow();
		
		engine.startNewRow();
		engine.slip(1);
		assertThat(engine.getTotalNumberOfStitchesOnCurrentNeedle(), is (10));
		engine.slip(10); 		// goes to the next needle
		assertThat(engine.getTotalNumberOfStitchesOnCurrentNeedle(), is (9));
		engine.slip(9);		// goes to the next needle
		assertThat(engine.getTotalNumberOfStitchesOnCurrentNeedle(), is (19));
	}
	
	@Test
	public void p2togBetweenNeedles() throws Exception {
		engine.purl(9);
		engine.purlTwoTogether();
		engine.purl(8);
		engine.purlTwoTogether();
		engine.purl(19);
		engine.endRow();
		
		// neutralizes flat vs. round
		engine.startNewRow();
		engine.knit(38);
		engine.endRow();
		
		engine.startNewRow();
		engine.slip(1);
		assertThat(engine.getTotalNumberOfStitchesOnCurrentNeedle(), is (10));
		engine.slip(10); 		// goes to the next needle
		assertThat(engine.getTotalNumberOfStitchesOnCurrentNeedle(), is (9));
		engine.slip(9);		// goes to the next needle
		assertThat(engine.getTotalNumberOfStitchesOnCurrentNeedle(), is (19));
	}
	
	@Test
	public void k3togBetweenNeedles() throws Exception {
		engine.knit(9);
		engine.knitThreeTogether();
		engine.knit(6);
		engine.knitThreeTogether();
		engine.knit(19);
		engine.endRow();
		
		// neutralizes flat vs. round
		engine.startNewRow();
		engine.knit(36);
		engine.endRow();
		
		engine.startNewRow();
		engine.slip(1);
		assertThat(engine.getTotalNumberOfStitchesOnCurrentNeedle(), is (10));
		engine.slip(10); 		// goes to the next needle
		assertThat(engine.getTotalNumberOfStitchesOnCurrentNeedle(), is (7));
		engine.slip(8);		// goes to the next needle
		assertThat(engine.getTotalNumberOfStitchesOnCurrentNeedle(), is (19));
	}

	@Test
	public void transferStitchesToPreviousNeedle() throws Exception {
		engine.knit(40);
		engine.startNewRow();
		engine.knit(40);
		engine.endRow();
		engine.arrangeStitchesOnNeedles(new int[] { 20, 5, 15 });
		engine.startNewRow();
		assertEquals(Direction.FORWARDS, engine.getDirection());
		assertEquals(20, engine.getTotalNumberOfStitchesOnCurrentNeedle());
		knit(20);
		// haven't switched over to the next needle yet
		assertEquals(20, engine.getTotalNumberOfStitchesOnCurrentNeedle());
		knit(5);
		// haven't switched over to the next needle yet
		assertEquals(5, engine.getTotalNumberOfStitchesOnCurrentNeedle());
		knit(15);
		assertEquals(15, engine.getTotalNumberOfStitchesOnCurrentNeedle());
		engine.endRow();
	}

	@Test
	@Override
	public void getStitchesRemainingOnCurrentNeedle() throws Exception {
		knit(5);
		assertEquals(5, engine.getStitchesRemainingOnCurrentNeedle());
		knit(5);
		assertEquals(0, engine.getStitchesRemainingOnCurrentNeedle());
		knit(5);
		assertEquals(5, engine.getStitchesRemainingOnCurrentNeedle());
		knit(5);
		assertEquals(0, engine.getStitchesRemainingOnCurrentNeedle());
		knit(5);
		assertEquals(15, engine.getStitchesRemainingOnCurrentNeedle());
		knit(15);
		assertEquals(0, engine.getStitchesRemainingOnCurrentNeedle());
		engine.endRow();
	}

	@Test
	@Override
	public void getTotalNumberOfStitchesOnCurrentNeedle() throws Exception {
		assertEquals(10, engine.getTotalNumberOfStitchesOnCurrentNeedle());
		knit(10);
		// haven't switched over to the next needle yet
		assertEquals(10, engine.getTotalNumberOfStitchesOnCurrentNeedle());
		knit(5);
		for (int i=0; i < 5; i++) {
			engine.increase(new Increase());
		}
		assertEquals(15, engine.getTotalNumberOfStitchesOnCurrentNeedle());
		knit(6);
		assertEquals(20, engine.getTotalNumberOfStitchesOnCurrentNeedle());
		knit(19);
		engine.endRow();
	}

	
	@Test
	public void transferStitchesToSpareNeedle() throws Exception {
		Needle stitchHolder = knittingFactory.createNeedle("stitch-holder", NeedleStyle.CIRCULAR); //$NON-NLS-1$
		knit(8);
		engine.transferStitchesToNeedle(stitchHolder, 4);
		knit(28);
		// take care of flat vs. round
		engine.startNewRow();
		engine.knit(36);
		engine.startNewRow();
		
		assertThat(engine.getTotalNumberOfStitchesOnCurrentNeedle(), is (8));

		List<Stitch> firstNeedleStitches = engine.getCurrentNeedle().getStitches();
		assertThat(firstNeedleStitches.get(0).getId(), is ("A")); //$NON-NLS-1$
		assertThat(firstNeedleStitches.get(1).getId(), is ("B")); //$NON-NLS-1$
		assertThat(firstNeedleStitches.get(2).getId(), is ("C")); //$NON-NLS-1$
		assertThat(firstNeedleStitches.get(3).getId(), is ("D")); //$NON-NLS-1$
		assertThat(firstNeedleStitches.get(4).getId(), is ("E")); //$NON-NLS-1$
		assertThat(firstNeedleStitches.get(5).getId(), is ("F")); //$NON-NLS-1$
		assertThat(firstNeedleStitches.get(6).getId(), is ("G")); //$NON-NLS-1$
		assertThat(firstNeedleStitches.get(7).getId(), is ("H")); //$NON-NLS-1$
		
		List<Stitch> stitchHolderStitches = stitchHolder.getStitches();
		
		assertThat(stitchHolderStitches.get(0).getId(), is ("I")); //$NON-NLS-1$
		assertThat(stitchHolderStitches.get(1).getId(), is ("J")); //$NON-NLS-1$
		assertThat(stitchHolderStitches.get(2).getId(), is ("K")); //$NON-NLS-1$
		assertThat(stitchHolderStitches.get(3).getId(), is ("L")); //$NON-NLS-1$
		
		// go to the second needle
		knit(10);
		List<Stitch> secondNeedleStitches = engine.getCurrentNeedle().getStitches();
		assertThat(secondNeedleStitches.get(0).getId(), is ("M")); //$NON-NLS-1$
		assertThat(secondNeedleStitches.get(1).getId(), is ("N")); //$NON-NLS-1$
		
	}

}
