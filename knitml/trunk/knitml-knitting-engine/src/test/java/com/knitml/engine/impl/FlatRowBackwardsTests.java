/**
 * 
 */
package com.knitml.engine.impl;

import com.knitml.engine.common.KnittingEngineException;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class FlatRowBackwardsTests extends FlatRowTests {

	@Override
	public void onSetUp() throws KnittingEngineException {
		knitter.startNewRow();
	}
}
