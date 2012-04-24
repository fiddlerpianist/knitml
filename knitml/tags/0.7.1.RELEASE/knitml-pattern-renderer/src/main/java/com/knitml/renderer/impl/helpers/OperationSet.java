package com.knitml.renderer.impl.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class OperationSet implements Iterable<Object> {

	public enum Type {
		REPEAT, ROW, USING_NEEDLE, INLINE_INSTRUCTION, FROM_STITCH_HOLDER, INC_INTO_NEXT_ST, OPERATION_GROUP
	}

	private List<Object> operations = new ArrayList<Object>();
	private String head;
	private String tail;
	private Type type;

	public OperationSet(Type type) {
		this.type = type;
	}

	public int size() {
		return operations.size();
	}

	public Object getOperation(int index) {
		return operations.get(index);
	}
	
	protected List<Object> getOperations() {
		return this.operations;
	}

	public void addOperationString(String operation) {
		operations.add(operation);
	}

	public void addOperation(SimpleInstruction operation) {
		operations.add(operation);
	}

	public void addOperationSet(OperationSet operationSet) {
		// JW 10/26/09: I believe this was added because rendering couldn't be guaranteed to make sense,
		// however nested repeats need to be supported by the spec
//		if (type == Type.REPEAT) {
//			throw new IllegalStateException("Cannot nest repeat elements");
//		}
		operations.add(operationSet);
	}

	public Iterator<Object> iterator() {
		return operations.iterator();
	}

	public Type getType() {
		return type;
	}

	public void setTail(String tail) {
		this.tail = tail;
	}

	public String getTail() {
		return tail;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getHead() {
		return head;
	}

}
