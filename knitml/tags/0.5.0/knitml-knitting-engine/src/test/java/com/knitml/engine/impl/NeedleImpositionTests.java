/**
 * 
 */
package com.knitml.engine.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.knitml.core.common.NeedleStyle;
import com.knitml.engine.KnittingFactory;
import com.knitml.engine.Needle;
import com.knitml.engine.Stitch;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.engine.common.NotEnoughStitchesException;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class NeedleImpositionTests {

	protected DefaultKnittingEngine engine;
	protected KnittingFactory knittingFactory = new DefaultKnittingFactory();
	protected Needle imposingNeedle = knittingFactory.createNeedle(
			"imposingNeedle", NeedleStyle.CIRCULAR);

	protected void knit(int numberOfStitches) throws KnittingEngineException {
		for (int i = 0; i < numberOfStitches; i++) {
			engine.knit();
		}
	}

	/**
	 * Sets up the engine with 20 stitches originally, 8 of which (6-13) are put
	 * on a stitch holder. The engine is left starting a new row in the forwards
	 * direction.
	 * 
	 * @throws Exception
	 */
	@Before
	public final void setUp() throws Exception {
		engine = new DefaultKnittingEngine(knittingFactory);
		// cast on 20 stitches
		engine.castOn(20, false);
		engine.startNewRow();
		// knitting forwards
		knit(5);
		// transfer stitches 6-13 to a non-active needle
		engine.transferStitchesToNeedle(imposingNeedle, 8);
		knit(7);
		// knitting backwards
		engine.startNewRow();
		knit(12);
		engine.startNewRow();
	}

	@Test
	public void basicWorkFromImposingNeedle() throws Exception {
		knit(2);

		engine.imposeNeedle(imposingNeedle);
		assertThat (engine.getCurrentNeedle(), is (imposingNeedle));
		knit(8);
		assertThat (engine.getStitchesRemainingInRow(), is (0));
		assertThat (engine.getTotalNumberOfStitchesInRow(), is (0));

		engine.unimposeNeedle();
		assertThat (engine.getCurrentNeedle(), is (not (imposingNeedle)));
		assertThat (engine.getStitchesRemainingInRow(), is (10));
		assertThat (engine.getTotalNumberOfStitchesInRow(), is (20));
		verifyStitchOrder(engine.getCurrentNeedle(), "ABFGHIJKLMCDENOPQRST");
	}

	@Test
	public void workPartiallyFromImposingNeedle() throws Exception {
		knit(2);

		engine.imposeNeedle(imposingNeedle);
		assertThat (engine.getCurrentNeedle(), is (imposingNeedle));
		knit(6);
		assertThat (engine.getStitchesRemainingInRow(), is (2));
		assertThat (engine.getTotalNumberOfStitchesInRow(), is (2));

		engine.unimposeNeedle();
		assertThat (engine.getCurrentNeedle(), is (not (imposingNeedle)));
		assertThat (engine.getStitchesRemainingInRow(), is (10));
		assertThat (engine.getTotalNumberOfStitchesInRow(), is (18));
		verifyStitchOrder(engine.getCurrentNeedle(), "ABFGHIJKCDENOPQRST");
		verifyStitchOrder(imposingNeedle, "LM");
	}

	@Test
	public void imposeNeedleAtBeginning() throws Exception {
		engine.imposeNeedle(imposingNeedle);
		assertThat (engine.getCurrentNeedle(), is (imposingNeedle));
		assertThat (engine.getStitchesRemainingInRow(), is (8));
		assertThat (engine.getTotalNumberOfStitchesInRow(), is (8));

		engine.unimposeNeedle();
		assertThat (engine.getCurrentNeedle(), is (not (imposingNeedle)));
		assertThat (engine.getStitchesRemainingInRow(), is (12));
		assertThat (engine.getTotalNumberOfStitchesInRow(), is (12));
		verifyStitchOrder(engine.getCurrentNeedle(), "ABCDENOPQRST");
		verifyStitchOrder(imposingNeedle, "FGHIJKLM");
	}

	
	
	@Test(expected=NotEnoughStitchesException.class)
	public void knitTooFarFromImposingNeedle() throws Exception {
		knit(2);
		engine.imposeNeedle(imposingNeedle);
		knit(9);
	}
	
	@Test
	public void knitTwoTogetherFromImposingNeedle() throws Exception {
		knit(2);
		engine.imposeNeedle(imposingNeedle);
		engine.knitTwoTogether();
		engine.knitTwoTogether();
		engine.knitTwoTogether();
		assertThat (engine.getCurrentNeedle(), is (imposingNeedle));
		assertThat (engine.getStitchesRemainingInRow(), is (2));

		engine.unimposeNeedle();
		assertThat (engine.getCurrentNeedle(), is (not (imposingNeedle)));
		assertThat (engine.getStitchesRemainingInRow(), is (10));
		assertThat (engine.getTotalNumberOfStitchesInRow(), is (15));
		verifyStitchOrder(engine.getCurrentNeedle(), "ABGIKCDENOPQRST");
		verifyStitchOrder(imposingNeedle, "LM");
	}

	@Test
	public void slipFromImposingNeedle() throws Exception {
		knit(2);
		engine.imposeNeedle(imposingNeedle);
		engine.slip(6);
		assertThat (engine.getCurrentNeedle(), is (imposingNeedle));
		assertThat (engine.getStitchesRemainingInRow(), is (2));

		engine.unimposeNeedle();
		assertThat (engine.getCurrentNeedle(), is (not (imposingNeedle)));
		assertThat (engine.getStitchesRemainingInRow(), is (10));
		assertThat (engine.getTotalNumberOfStitchesInRow(), is (18));
		verifyStitchOrder(engine.getCurrentNeedle(), "ABFGHIJKCDENOPQRST");
		verifyStitchOrder(imposingNeedle, "LM");
	}
	
	
	protected void verifyStitchOrder(Needle needle, String stitchOrder)
			throws Exception {
		List<Stitch> stitches = needle.getStitches();
		for (int i = 0; i < stitchOrder.length(); i++) {
			assertThat(stitches.get(i).getId(), is(String.valueOf(stitchOrder
					.charAt(i))));
		}
	}

}
