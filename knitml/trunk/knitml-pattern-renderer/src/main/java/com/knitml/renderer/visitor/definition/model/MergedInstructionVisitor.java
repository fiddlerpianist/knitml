package com.knitml.renderer.visitor.definition.model;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.MergeType;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.MergedInstruction;
import com.knitml.renderer.common.RenderingException;
import com.knitml.renderer.context.ContextUtils;
import com.knitml.renderer.context.PatternRepository;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.visitor.controller.InstructionController;
import com.knitml.renderer.visitor.impl.AbstractRenderingVisitor;
import com.knitml.renderer.visitor.impl.DefaultVisitorFactory;

public class MergedInstructionVisitor extends AbstractRenderingVisitor {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory
			.getLogger(MergedInstructionVisitor.class);

	public boolean begin(Object element, RenderingContext context)
			throws RenderingException {
		MergedInstruction mergedInstruction = (MergedInstruction) element;
		PatternRepository repository = context.getPatternRepository();
		if (mergedInstruction.getType() != MergeType.PHYSICAL) {
			throw new NotImplementedException(
					"Only physical merge types are supported at this time");
		}
		// even though the validation visitor doesn't go into the model, make it
		// clear we don't want to, either
		return false;
	}

	public void end(Object element, RenderingContext context) {
		MergedInstruction mergedInstructionDefinition = (MergedInstruction) element;
		String label = ContextUtils.deriveLabel(mergedInstructionDefinition,
				context.getPatternRepository());
		Instruction instruction = context.getKnittingContext()
				.getPatternRepository().getBlockInstruction(
						mergedInstructionDefinition.getId());
		context.getRenderer().beginInstructionDefinition(instruction, label);
		// do we want to hard code the implementation here?
		InstructionController embeddedController = new InstructionController(new DefaultVisitorFactory());
		embeddedController.visitInstruction(instruction, context);
		context.getRenderer().endInstructionDefinition();
	}
}
