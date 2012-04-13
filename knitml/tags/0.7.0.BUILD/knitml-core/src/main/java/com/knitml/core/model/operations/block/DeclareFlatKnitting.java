package com.knitml.core.model.operations.block;

import com.knitml.core.common.Side;
import com.knitml.core.model.operations.BlockOperation;


public class DeclareFlatKnitting implements BlockOperation {
	
	protected Side nextRowSide;

	public Side getNextRowSide() {
		return nextRowSide;
	}

}
