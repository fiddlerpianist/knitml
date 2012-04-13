package com.knitml.renderer.context;

import com.knitml.core.compatibility.Stack;
import com.knitml.core.model.operations.CompositeOperation;

public class PatternState {
	
	// tracks the current operation tree so that siblings and parents in the tree can be examined
	private Stack<CompositeOperation> operationTree = new Stack<CompositeOperation>();
	// If an instruction is currently being defined, this will be set with that info
	private InstructionInfo currentInstructionInfo;
	// The last row that was hit whose row number was defined
	private int lastExpressedRowNumber = 0;
	private Object engineStateMemento;


	public int getLastExpressedRowNumber() {
		return lastExpressedRowNumber;
	}

	public void setLastExpressedRowNumber(int nextRowNumber) {
		this.lastExpressedRowNumber = nextRowNumber;
	}

	public boolean hasCurrentInstruction() {
		return currentInstructionInfo != null;
	}
	
	public void clearCurrentInstructionInfo() {
		this.currentInstructionInfo = null;
	}

	public InstructionInfo getCurrentInstructionInfo() {
		return currentInstructionInfo;
	}

	public void setCurrentInstructionInfo(InstructionInfo currentInstruction) {
		this.currentInstructionInfo = currentInstruction;
	}

	public Stack<CompositeOperation> getOperationTree() {
		return operationTree;
	}

	public void setOperationTree(Stack<CompositeOperation> operationStack) {
		this.operationTree = operationStack;
	}

	public void resetLastExpressedRowNumber() {
		this.lastExpressedRowNumber = 0;
	}

	public void saveEngineState(Object memento) {
		this.engineStateMemento = memento;
	}

	public Object retrieveSavedEngineState() {
		return this.engineStateMemento;
	}

}
