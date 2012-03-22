package com.knitml.renderer.visitor;

import java.util.List;

import javax.inject.Inject;

import com.knitml.core.model.operations.CompositeOperation;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.InlineOperation;
import com.knitml.core.model.operations.Operation;
import com.knitml.core.model.operations.block.Instruction;
import com.knitml.core.model.operations.block.Row;
import com.knitml.core.model.operations.inline.InlineInstruction;
import com.knitml.renderer.Renderer;
import com.knitml.renderer.event.EventHandler;
import com.knitml.renderer.event.EventHandlerFactory;
import com.knitml.renderer.nameresolver.DirectionsNameResolver;
import com.knitml.renderer.nameresolver.NameResolver;

public class EmbeddedVisitor {

	private EventHandlerFactory eventHandlerFactory;
	private NameResolver directionsNameResolver = new DirectionsNameResolver();

	@Inject
	public EmbeddedVisitor(EventHandlerFactory eventHandlerFactory) {
		this.eventHandlerFactory = eventHandlerFactory;
	}

	public void visitInstruction(Instruction instruction,
			Renderer renderer) {
		if (instruction.hasRows()) {
			for (Row row : instruction.getRows()) {
				visit(row, renderer);
			}
		} else {
			visit(instruction.getForEachRowInInstruction(), renderer);
		}
	}

	public void visitInlineInstruction(InlineInstruction instruction,
			Renderer renderer) {
		List<InlineOperation> subOperations = instruction.getOperations();
		for (Operation subOperation : subOperations) {
			visit(subOperation, renderer);
		}
	}

	public void visitDiscreteInlineOperation(DiscreteInlineOperation operation,
			Renderer renderer) {
		visit(operation, renderer);
	}
	
	protected void visit(Operation operation, Renderer renderer) {
		EventHandler eventHandler = eventHandlerFactory
				.findEventHandlerFromClassName(operation, directionsNameResolver);
		boolean result = eventHandler.begin(operation, renderer);
		if (result) {
			if (operation instanceof Row && ((Row)operation).getInformation() != null) {
				visit(((Row)operation).getInformation(), renderer);
			}
			if (operation instanceof CompositeOperation) {
				List<? extends Operation> subOperations = ((CompositeOperation) operation)
						.getOperations();
				for (Operation subOperation : subOperations) {
					visit(subOperation, renderer);
				}
			}
			if (operation instanceof Row && ((Row)operation).getFollowupInformation() != null) {
				visit(((Row)operation).getFollowupInformation(), renderer);
			}
		}
		eventHandler.end(operation, renderer);
	}
}
