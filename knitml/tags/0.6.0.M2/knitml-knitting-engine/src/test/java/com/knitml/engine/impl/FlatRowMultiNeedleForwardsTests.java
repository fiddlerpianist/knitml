/**
 * 
 */
package com.knitml.engine.impl;


/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class FlatRowMultiNeedleForwardsTests extends FlatRowMultiNeedleTests {

	@Override
	protected void onSetUp() throws Exception {
		engine.startNewRow();
		knit(40);
		engine.resetRowNumber();
		engine.startNewRow();
	}
	
}
