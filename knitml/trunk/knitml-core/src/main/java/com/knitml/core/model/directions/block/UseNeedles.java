package com.knitml.core.model.directions.block;

import java.util.List;

import com.knitml.core.model.directions.BlockOperation;
import com.knitml.core.model.header.Needle;


public class UseNeedles implements BlockOperation {
	
	protected List<Needle> needles;
	protected boolean silentRendering;
	
	public List<Needle> getNeedles() {
		return needles;
	}
	public boolean isSilentRendering() {
		return silentRendering;
	}

}
