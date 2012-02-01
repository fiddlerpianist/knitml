package com.knitml.core.model.directions;

import java.util.List;


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
