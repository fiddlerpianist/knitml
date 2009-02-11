/**
 * 
 */
package com.knitml.engine.impl;

import org.junit.Ignore;


/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class FlatRowMultiNeedleBackwardsTests extends FlatRowMultiNeedleTests {

	@Override
	public void onSetUp() throws Exception {
		knitter.startNewRow();
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
	
}
