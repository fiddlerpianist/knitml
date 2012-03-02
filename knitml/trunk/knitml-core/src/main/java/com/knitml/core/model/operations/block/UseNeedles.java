package com.knitml.core.model.operations.block;

import java.util.List;

import com.knitml.core.model.common.Needle;
import com.knitml.core.model.operations.BlockOperation;


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
