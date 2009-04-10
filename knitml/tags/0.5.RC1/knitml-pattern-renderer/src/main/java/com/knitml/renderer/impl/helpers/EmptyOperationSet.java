package com.knitml.renderer.impl.helpers;

import java.util.Collections;
import java.util.Iterator;


public class EmptyOperationSet extends OperationSet {

	public EmptyOperationSet() {
		super(OperationSet.Type.EMPTY);
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public Object getOperation(int index) {
		return null;
	}

	@Override
	public void addOperationString(String operation) {
	}

	@Override
	public void addOperation(SimpleInstruction operation) {
	}

	public void addOperationSet(EmptyOperationSet operationSet) {
	}

	@Override
	public Iterator<Object> iterator() {
		return Collections.emptySet().iterator();
	}

	@Override
	public Type getType() {
		return null;
	}

	@Override
	public void setTail(String tail) {
	}

	@Override
	public String getTail() {
		return null;
	}

	@Override
	public void setHead(String head) {
	}

	@Override
	public String getHead() {
		return null;
	}

}
