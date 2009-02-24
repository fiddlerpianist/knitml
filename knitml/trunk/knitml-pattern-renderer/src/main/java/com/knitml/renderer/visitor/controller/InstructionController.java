package com.knitml.renderer.visitor.controller;

import java.util.List;

import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.directions.Operation;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.RenderingVisitor;
import com.knitml.renderer.visitor.VisitorFactory;

public class InstructionController {
	private VisitorFactory visitorFactory;

	public InstructionController(VisitorFactory visitorFactory) {
		this.visitorFactory = visitorFactory;
	}

	public void visitInstruction(Instruction instruction,
			RenderingContext context) {
		if (instruction.hasRows()) {
			for (Row row : instruction.getRows()) {
				visit(row, context);
			}
		}
	}

	protected void visit(Operation operation, RenderingContext context) {
		RenderingVisitor visitor = visitorFactory.findVisitorFromClassName(operation);
		boolean result = visitor.begin(operation, context);
		if (result) {
			visit(operation, context);
			if (operation instanceof CompositeOperation) {
				List<? extends Operation> subOperations = ((CompositeOperation)operation).getOperations();
				for (Operation subOperation : subOperations) {
					visit(subOperation, context);
				}
			}
		}
		visitor.end(operation, context);
	}
}
