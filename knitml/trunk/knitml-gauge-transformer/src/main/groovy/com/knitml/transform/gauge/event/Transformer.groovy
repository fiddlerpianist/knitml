/** 
 *  Copyright 2008, 2009 Jonathan Whitall
 * 
 *  This file is part of Gauge Transformer.
 * 
 *  Gauge Transformer is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Gauge Transformer is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Gauge Transformer.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.knitml.transform.gauge.event

import static java.lang.Math.abs
import static java.lang.Math.round

import javax.measure.Measurable

import com.knitml.core.model.operations.CompositeOperation
import com.knitml.core.model.operations.Operation
import com.knitml.core.model.operations.block.CastOn
import com.knitml.core.model.operations.block.Information
import com.knitml.core.model.operations.block.Row
import com.knitml.core.model.operations.information.Message
import com.knitml.core.model.operations.inline.Decrease
import com.knitml.core.model.operations.inline.DoubleDecrease
import com.knitml.core.model.operations.inline.Increase
import com.knitml.core.model.operations.inline.InlineInstructionRef
import com.knitml.core.model.operations.inline.InlinePickUpStitches
import com.knitml.core.model.operations.inline.Knit
import com.knitml.core.model.operations.inline.Purl
import com.knitml.core.model.operations.inline.Repeat
import com.knitml.core.model.operations.inline.Repeat.Until
import com.knitml.core.model.pattern.Pattern
import com.knitml.core.units.Units

/**
 * @author Jonathan Whitall
 */

class Transformer {
	
	Measurable originalGauge
	Measurable targetGauge
	
	/**
	 * The number of stitches in the current row AFTER it has been worked.
	 */
	private int currentRowStitchCount
	/**
	 * The number of stitches in the previous row AFTER it has been worked.
	 * It's also the number of stitches in the current row BEFORE it has been worked.
	 */
	private int previousRowStitchCount
	
	def visit (CastOn castOn) {
		int originalCastOn = castOn.numberOfStitches
		int delta = calculateStitchDifference (originalCastOn)
		castOn.numberOfStitches += delta
		previousRowStitchCount = originalCastOn
	}
	
	def visit (Operation operation) {
		switch (operation) {
			case CompositeOperation:
				operation.operations.each { visit it }
				break
			case Knit:
			case Purl:
			case Increase:
			case Decrease:
			case DoubleDecrease:
			case InlinePickUpStitches:
				countOperation operation
				break
			default:
				break
		}
	}
	
	def visit(InlineInstructionRef ref) {
		visit ref.referencedInstruction
	}
	
	def visit (Row row) {
		currentRowStitchCount = 0
		// instructions for how to change the row about to be knit
		List messageList = []
		int stitchDifference = calculateStitchDifference (previousRowStitchCount)
		if (stitchDifference != 0) {
			def diff
			if (stitchDifference > 0) {
				diff = "${abs(stitchDifference)} more"
			} else if (stitchDifference < 0) {
				diff = "${abs(stitchDifference)} fewer"
			}
			def message = new Message()
			message.label = "with $diff st"
			messageList << message
		}
		row.operations.each { visit it }
		if (calculateStitchDifference (currentRowStitchCount) != 0 && currentRowStitchCount != previousRowStitchCount) {
			def message = new Message()
			message.label = "end with ${calculateTargetStitchCount(currentRowStitchCount)}"
			messageList << message
		}
		if (!messageList.isEmpty()) {
			row.information = new Information()
			row.information.details = messageList
		}
		previousRowStitchCount = currentRowStitchCount
	}
	
	def visit (Repeat repeat) {
		if (repeat.until != Until.TIMES) {
			throw new RuntimeException("Only the 'times' repeat type is currently supported")
		}
		for (int i = 0; i < repeat.value; i++) {
			repeat.operations.each { visit it }
		}
	}
	
	private def countOperation(op) {
		currentRowStitchCount += op.numberOfTimes == null ? 1 : op.numberOfTimes  
	}
	
	private int calculateStitchDifference(int original) {
		int targetStitches = calculateTargetStitchCount (original)
		// positive number if increase is needed, negative if decrease is needed
		return targetStitches - original
	}
	
	private int calculateTargetStitchCount(int original) {
		return round(original / originalGauge.doubleValue(Units.STITCHES_PER_INCH) * targetGauge.doubleValue(Units.STITCHES_PER_INCH))
	}
	
	def run(Pattern pattern) {
		if (originalGauge == null) {
			originalGauge = pattern.generalInformation?.gauge?.stitchGauge
		}
		if (originalGauge == null) {
			throw new RuntimeException("No original gauge could be found, and it was not specified. Cannot transform")
		}
		if (targetGauge == null) {
			throw new RuntimeException("No target gauge was specified. Cannot transform")
		}
		visit pattern.directions
		// transform the specified gauge of the pattern (if provided)
		pattern.generalInformation?.gauge?.stitchGauge = targetGauge
	}
	
}