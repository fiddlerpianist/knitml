package com.knitml.core.model.directions.block;

import com.knitml.core.common.Side;
import com.knitml.core.model.directions.BlockOperation;


public class DeclareFlatKnitting implements BlockOperation {
	
	protected Side nextRowSide;

	public Side getNextRowSide() {
		return nextRowSide;
	}

}
