package com.knitml.core.model.operations.block;

import java.util.List;

import com.knitml.core.model.common.Needle;
import com.knitml.core.model.operations.BlockOperation;


public class GraftTogether implements BlockOperation {
	
	protected List<Needle> needles;
	
	public List<Needle> getNeedles() {
		return needles;
	}

}
