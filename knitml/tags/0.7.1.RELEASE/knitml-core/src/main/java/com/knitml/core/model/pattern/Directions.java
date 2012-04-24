package com.knitml.core.model.pattern;

import java.util.List;

import com.knitml.core.model.operations.BlockOperation;
import com.knitml.core.model.operations.CompositeOperation;
import com.knitml.core.model.operations.Operation;


/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class Directions implements BlockOperation, CompositeOperation {

	protected List<Operation> operations;
	
	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	public List<Operation> getOperations() {
		return operations;
	}
	
}
