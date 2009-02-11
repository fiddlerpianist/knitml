package com.knitml.validation.visitor.instruction.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.Measurable;
import javax.measure.Measure;
import javax.measure.quantity.Length;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.NoInstructionFoundException;
import com.knitml.core.common.StitchesOnNeedle;
import com.knitml.core.common.ValidationException;
import com.knitml.core.model.Identifiable;
import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.RepeatInstruction;
import com.knitml.core.model.directions.block.RepeatInstruction.Until;
import com.knitml.core.model.directions.expression.Expression;
import com.knitml.core.units.RowGauge;
import com.knitml.core.units.Units;
import com.knitml.engine.Needle;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.NeedleNotFoundException;
import com.knitml.validation.visitor.instruction.Visitor;
import com.knitml.validation.visitor.instruction.impl.AbstractValidationVisitor;
import com.knitml.validation.visitor.util.NeedleUtils;

@SuppressWarnings("unchecked")
public class RepeatInstructionVisitor extends AbstractValidationVisitor {

	private final static Logger log = LoggerFactory
			.getLogger(RepeatInstructionVisitor.class);

	public void visit(Object element, KnittingContext context)
			throws KnittingEngineException {
		RepeatInstruction operation = (RepeatInstruction) element;
		Identifiable identifiable = operation.getRef();
		Instruction instruction = context.getPatternRepository()
				.getBlockInstruction(identifiable.getId());
		if (instruction == null) {
			throw new NoInstructionFoundException(
					"Could not find instruction for [repeat-instruction]");
		}
		Until until = operation.getUntil();
		Object value = operation.getValue();
		context.getPatternState().setReplayMode(true);
		try {
			switch (until) {
			case ADDITIONAL_TIMES:
				handleAdditionalTimes(getRequiredInteger(value), instruction,
						context);
				break;
			case UNTIL_DESIRED_LENGTH:
				handleUntilDesiredLength(instruction, context);
				break;
			case UNTIL_STITCHES_REMAIN:
				handleUntilStitchesRemain(getRequiredInteger(value),
						instruction, context);
				break;
			case UNTIL_STITCHES_REMAIN_ON_NEEDLES:
				handleUntilStitchesRemainOnNeedles(value, instruction, context);
				break;
			case UNTIL_MEASURES:
				handleUntilMeasures(getRequiredLength(value), instruction,
						context);
				break;
			case UNTIL_EQUALS:
				handleUntilEquals(value, instruction, context);
				break;
			}
			context.getPatternState().removeRepeatCount(instruction);
		} finally {
			context.getPatternState().setReplayMode(false);
		}
	}

	private Measurable<Length> getRequiredLength(Object value) {
		if (value == null || !(value instanceof Measure)) {
			throw new ValidationException(
					"The value property of the RepeatInstruction object must be an integer when until is defined as ADDITIONAL_TIMES");
		}
		Measure measure = (Measure) value;
		// validate that it's a Measure<Length>
		measure.getUnit().asType(Length.class);
		return measure;
	}

	private Integer getRequiredInteger(Object value) {
		if (value == null || !(value instanceof Integer)) {
			throw new ValidationException(
					"The value property of the RepeatInstruction object must be an integer when until is defined as ADDITIONAL_TIMES");
		}
		return (Integer) value;
	}

	private void handleUntilEquals(Object value,
			Instruction instructionToRepeat, KnittingContext context)
			throws KnittingEngineException {

		if (!(value instanceof List)) {
			throw new ValidationException(
					"A RepeatInstruction element must specify a list of expressions when Until is set to UNTIL_EQUALS");
		}
		List<Expression> expressions = (List<Expression>) value;
		Visitor visitor = getVisitorFactory().findVisitorFromClassName(
				instructionToRepeat);

		EqualsEvaluator evaluator = new EqualsEvaluator(context);
		while (!evaluator.areEqual(expressions)) {
			context.getPatternState().incrementRepeatCount(instructionToRepeat);
			visitor.visit(instructionToRepeat, context);
		}
	}

	private void handleUntilStitchesRemainOnNeedles(Object value,
			Instruction instructionToRepeat, KnittingContext context)
			throws KnittingEngineException {

		if (!(value instanceof List)) {
			throw new ValidationException(
					"A RepeatInstruction element must specify a list of needles when Until is set to UNTIL_STITCHES_REMAIN_ON_NEEDLES");
		}
		List<StitchesOnNeedle> stitchesOnNeedles = (List<StitchesOnNeedle>) value;

		// first build a map of Needles and their target stitch counts
		Map<Needle, Integer> needlesToStitches = new HashMap<Needle, Integer>();
		for (StitchesOnNeedle stitchesOnNeedle : stitchesOnNeedles) {
			String id = stitchesOnNeedle.getNeedle().getId();
			Needle engineNeedle = NeedleUtils.lookupNeedle(id, context);
			if (engineNeedle == null) {
				throw new NeedleNotFoundException(id);
			}
			Integer targetNumberOfStitches = stitchesOnNeedle
					.getNumberOfStitches();
			if (targetNumberOfStitches == null) {
				throw new ValidationException(engineNeedle.toString()
						+ " must specify a value for the stitch count");
			}
			needlesToStitches.put(engineNeedle, targetNumberOfStitches);
		}

		// now get the instruction to repeat
		Visitor visitor = getVisitorFactory().findVisitorFromClassName(
				instructionToRepeat);

		// start off by assuming we need to repeat the instruction
		boolean shouldRepeat = true;
		// repeat until we determine that we should no longer repeat (i.e. all
		// needles match their target stitch count)
		while (shouldRepeat) {
			for (Needle needle : needlesToStitches.keySet()) {
				// if this needle's stitch count does not equal the target
				// stitch count for this needle, continue repeating
				shouldRepeat = needle.getTotalStitches() != needlesToStitches
						.get(needle);
				if (shouldRepeat) {
					// if we already know we have to repeat, break out of the
					// for loop to optimize
					break;
				}
			}
			if (shouldRepeat) {
				// if we get here, at least one needle indicated that the stitch
				// count did not equal, so we should continue repeating
				context.getPatternState().incrementRepeatCount(instructionToRepeat);
				visitor.visit(instructionToRepeat, context);
			}
		}

	}

	private void handleUntilMeasures(Measurable<Length> targetMeasurement,
			Instruction instructionToRepeat, KnittingContext context)
			throws KnittingEngineException {
		Visitor visitor = getVisitorFactory().findVisitorFromClassName(
				instructionToRepeat);
		Measurable<RowGauge> rowGauge = context.getPatternRepository()
				.getRowGauge();
		if (rowGauge == null) {
			log
					.warn("The UNTIL_MEASURES specification cannot be fully processed "
							+ "because a row gauge was not specified in the pattern");
		} else {
			double untilMeasuresInches = targetMeasurement
					.doubleValue(Units.INCH);
			double rowsPerInch = rowGauge.doubleValue(Units.ROWS_PER_INCH);
			int repeats = (int) Math.ceil((untilMeasuresInches * rowsPerInch)
					/ instructionToRepeat.getRows().size());
			for (int i = 0; i < repeats - 1; i++) {
				context.getPatternState().incrementRepeatCount(instructionToRepeat);
				visitor.visit(instructionToRepeat, context);
			}
		}
	}

	private void handleUntilStitchesRemain(int targetNumberOfStitches,
			Instruction instructionToRepeat, KnittingContext context)
			throws KnittingEngineException {
		Visitor visitor = getVisitorFactory().findVisitorFromClassName(
				instructionToRepeat);
		while (context.getEngine().getTotalNumberOfStitchesInRow() != targetNumberOfStitches) {
			context.getPatternState().incrementRepeatCount(instructionToRepeat);
			visitor.visit(instructionToRepeat, context);
		}
	}

	private void handleUntilDesiredLength(Instruction instructionToRepeat,
			KnittingContext context) {
		log
				.warn("An UNTIL_DESIRED_LENGTH parameter on a RepeatInstruction was specified. This means that the engine will not replay this instruction.");
	}

	private void handleAdditionalTimes(int numberOfRepeats,
			Instruction instructionToRepeat, KnittingContext context)
			throws KnittingEngineException {
		Visitor visitor = getVisitorFactory().findVisitorFromClassName(
				instructionToRepeat);
		for (int i = 0; i < numberOfRepeats; i++) {
			context.getPatternState().incrementRepeatCount(instructionToRepeat);
			visitor.visit(instructionToRepeat, context);
		}
	}

}
