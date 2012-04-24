package com.knitml.core.model.operations.inline;

import com.knitml.core.model.operations.InlineOperation;



public class Turn implements InlineOperation {
	
	protected Integer stitchesLeft;
	protected boolean informUnworkedStitches;
	
	public void setStitchesLeft(Integer stitchesLeft) {
		this.stitchesLeft = stitchesLeft;
	}
	public void setInformUnworkedStitches(boolean informUnworkedStitches) {
		this.informUnworkedStitches = informUnworkedStitches;
	}
	public Integer getStitchesLeft() {
		return stitchesLeft;
	}
	public boolean isInformUnworkedStitches() {
		return informUnworkedStitches;
	}
	
}
