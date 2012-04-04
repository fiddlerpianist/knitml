package com.knitml.transform.gauge.event

import static java.lang.Math.abs
import static java.lang.Math.round

import javax.measure.Measurable

import com.knitml.core.model.operations.block.ArrangeStitchesOnNeedles
import com.knitml.core.model.operations.block.CastOn
import com.knitml.core.model.operations.block.Information
import com.knitml.core.model.operations.block.Row
import com.knitml.core.model.operations.information.Message
import com.knitml.core.model.operations.information.NumberOfStitches
import com.knitml.core.model.pattern.Pattern
import com.knitml.core.units.Units
import com.knitml.validation.context.KnittingContext

/**
 * @author Jonathan Whitall
 */

class TransformHandler {

	Measurable originalGauge
	Measurable targetGauge

	/**
	 * The number of stitches in the current row BEFORE it has been worked.
	 */
	private int stitchCountAtRowStart
	private List messageList

	void end(CastOn castOn, KnittingContext context) {
		int originalCastOn = castOn.numberOfStitches
		int delta = calculateStitchDifference (originalCastOn)
		if (delta != 0) {
			castOn.annotation = produceStitchDifferenceMessage(delta) + " than original gauge"
		}
		castOn.numberOfStitches = originalCastOn + delta
	}

	void end(NumberOfStitches numberOfStitches, KnittingContext context) {
		numberOfStitches.number = calculateTargetStitchCount(numberOfStitches.number)
	}

	void end(ArrangeStitchesOnNeedles arrStOnNeedle, KnittingContext context) {
		def originalCount = 0
		def targetCount = 0
		arrStOnNeedle.needles.each {
			int original = it.numberOfStitches
			originalCount = originalCount + original
			int target = calculateTargetStitchCount (it.numberOfStitches)
			targetCount = targetCount + target
			it.numberOfStitches = target
		}
		if (targetCount != calculateTargetStitchCount(originalCount)) {
			def targetNeedle = arrStOnNeedle.needles.get(arrStOnNeedle.needles.size() - 1)
			targetNeedle.numberOfStitches = targetNeedle.numberOfStitches + (calculateTargetStitchCount(originalCount) - targetCount)
		}
	}

	void begin(Pattern pattern, KnittingContext context) {
		if (originalGauge == null) {
			originalGauge = pattern.generalInformation?.gauge?.stitchGauge
			pattern.generalInformation?.gauge?.stitchGauge = targetGauge
			stitchCountAtRowStart = 0
			messageList = null
		}
		if (originalGauge == null) {
			throw new CannotTransformGaugeException("No original gauge could be found, and it was not specified. Cannot transform")
		}
		if (targetGauge == null) {
			throw new RuntimeException("No target gauge was specified. Cannot transform")
		}
	}

	void begin(Row row, KnittingContext context) {
		messageList = []
		stitchCountAtRowStart = context.engine.totalNumberOfStitchesInRow
		// instructions for how to change the row about to be knit
		int stitchDifference = calculateStitchDifference (stitchCountAtRowStart)
		if (stitchDifference != 0) {
			def message = new Message()
			//			message.label = "with " + produceStitchDifferenceMessage(stitchDifference)
			message.label = "with ${calculateTargetStitchCount(stitchCountAtRowStart)} st instead of ${stitchCountAtRowStart}"
			messageList << message
		}
	}

	def end(Row row, KnittingContext context) {
		def stitchCountAtRowEnd = context.engine.totalNumberOfStitchesInRow
		def originalDiff = stitchCountAtRowEnd - stitchCountAtRowStart
		def modifiedDiff = calculateTargetStitchCount(stitchCountAtRowEnd) - calculateTargetStitchCount(stitchCountAtRowStart)
		if (modifiedDiff != originalDiff) {
			def msg
			boolean none = false
			if (modifiedDiff > 0) {
				msg = "inc. ${abs(modifiedDiff)}"
			} else if (modifiedDiff < 0) {
				msg = "dec. ${abs(modifiedDiff)}"
			} else {
				none = true
			}
			def message = new Message()
			if (none) {
				message.label = "no st change instead of ${abs(originalDiff)} st change"
			} else {
				message.label = "${msg} st instead of ${abs(originalDiff)}"
			}
			messageList << message
		}
		if (!messageList.isEmpty()) {
			row.information = new Information()
			row.information.details = messageList
		}
	}

	private int calculateStitchDifference(int original) {
		int targetStitches = calculateTargetStitchCount (original)
		// positive number if increase is needed, negative if decrease is needed
		return targetStitches - original
	}

	private String produceStitchDifferenceMessage(int stitchDifference) {
		def diff
		if (stitchDifference > 0) {
			diff = "${abs(stitchDifference)} more"
		} else if (stitchDifference < 0) {
			diff = "${abs(stitchDifference)} fewer"
		}
		"$diff st"
	}

	private int calculateTargetStitchCount(int original) {
		return round(original / originalGauge.doubleValue(Units.STITCHES_PER_INCH) * targetGauge.doubleValue(Units.STITCHES_PER_INCH))
	}

}