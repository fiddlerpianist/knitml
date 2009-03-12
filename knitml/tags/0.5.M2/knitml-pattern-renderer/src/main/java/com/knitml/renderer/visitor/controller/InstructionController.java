package com.knitml.renderer.visitor.controller;

import java.util.List;

import com.knitml.core.model.directions.CompositeOperation;
import com.knitml.core.model.directions.InlineOperation;
import com.knitml.core.model.directions.Operation;
import com.knitml.core.model.directions.block.ForEachRowInInstruction;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.core.model.directions.inline.InlineInstruction;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.event.EventFactory;
import com.knitml.renderer.event.RenderingEvent;

public class InstructionController {
	private EventFactory visitorFactory;

	public InstructionController(EventFactory visitorFactory) {
		this.visitorFactory = visitorFactory;
	}

	public void visitInstruction(Instruction instruction,
			RenderingContext context) {
		if (instruction.hasRows()) {
			for (Row row : instruction.getRows()) {
				visit(row, context);
			}
		} else {
			visit(instruction.getForEachRowInInstruction(), context);
		}
	}

	public void visitInlineInstruction(InlineInstruction instruction,
			RenderingContext context) {
		List<InlineOperation> subOperations = instruction.getOperations();
		for (Operation subOperation : subOperations) {
			visit(subOperation, context);
		}
	}

	protected void visit(Operation operation, RenderingContext context) {
		RenderingEvent visitor = visitorFactory
				.findVisitorFromClassName(operation);
		boolean result = visitor.begin(operation, context);
		if (result) {
			if (operation instanceof CompositeOperation) {
				List<? extends Operation> subOperations = ((CompositeOperation) operation)
						.getOperations();
				for (Operation subOperation : subOperations) {
					visit(subOperation, context);
				}
			}
		}
		visitor.end(operation, context);
	}
}
