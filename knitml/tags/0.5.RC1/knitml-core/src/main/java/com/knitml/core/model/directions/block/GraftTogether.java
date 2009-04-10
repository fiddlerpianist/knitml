package com.knitml.core.model.directions.block;

import java.util.List;

import com.knitml.core.model.directions.BlockOperation;
import com.knitml.core.model.header.Needle;


public class GraftTogether implements BlockOperation {
	
	protected List<Needle> needles;
	
	public List<Needle> getNeedles() {
		return needles;
	}

}
