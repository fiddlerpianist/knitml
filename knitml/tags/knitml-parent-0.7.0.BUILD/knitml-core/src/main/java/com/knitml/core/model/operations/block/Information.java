package com.knitml.core.model.operations.block;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.model.operations.BlockOperation;

public class Information implements BlockOperation {

	protected List<Object> details = new ArrayList<Object>();

	public void setDetails(List<Object> details) {
		this.details = details;
	}

	public List<Object> getDetails() {
		return details;
	}

}
