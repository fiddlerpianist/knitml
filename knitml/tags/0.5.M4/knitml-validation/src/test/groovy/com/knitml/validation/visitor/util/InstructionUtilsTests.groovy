package com.knitml.validation.visitor.util

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static com.knitml.validation.visitor.util.InstructionUtils.createExpandedRows
import static com.knitml.validation.visitor.util.InstructionUtils.areRowsConsecutive

import java.util.ArrayList
import java.util.List

import org.junit.Test
import org.junit.runner.JUnitCore
import org.junit.runner.RunWith
import org.junit.internal.runners.JUnit4ClassRunner

import com.knitml.core.model.directions.block.Instruction
import com.knitml.validation.common.InvalidStructureException

@RunWith(JUnit4ClassRunner.class)
class InstructionUtilsTests {
	
	@Test
	void consecutiveRows() {
		def rows = [1,2,3,4,5]
		assertThat (areRowsConsecutive(rows), is (true))
	}

	@Test
	void notConsecutiveRows() {
		def rows = [1,3,4,5]
		assertThat (areRowsConsecutive(rows), is (false))
	}
	
	@Test
	void noRowsAtAll() {
		def rows = []
		assertThat (areRowsConsecutive(rows), is (true))
	}
	
	@Test
	void processableInstruction() {
		TestRow first = new TestRow([1,3,5])
		TestRow second = new TestRow([2,4,6,7])
		TestRow third = new TestRow([8])
		List rows = [first,second,third]
		Instruction instruction = new Instruction("thingy1",null,null,rows)
		def result = createExpandedRows (instruction)
		assertThat result.rows[0], is (first)
		assertThat result.rows[1], is (second)
		assertThat result.rows[2], is (first)
		assertThat result.rows[3], is (second)
		assertThat result.rows[4], is (first)
		assertThat result.rows[5], is (second)
		assertThat result.rows[6], is (second)
		assertThat result.rows[7], is (third)
	}
	
	@Test
	void processableInstructionWithNoRowNumbers() {
		TestRow first = new TestRow(null)
		TestRow second = new TestRow(null)
		TestRow third = new TestRow(null)
		List rows = [first,second,third]
		Instruction instruction = new Instruction("thingy1",null,null,rows)
		def result = createExpandedRows (instruction)
		assertThat result.rows[0], is (first)
		assertThat result.rows[1], is (second)
		assertThat result.rows[2], is (third)
	}
	
	@Test(expected=InvalidStructureException)
	void notProcessableInstruction() {
		TestRow first = new TestRow([1,3,5])
		List rows = [first]
		Instruction instruction = new Instruction("thingy1",null,null,rows)
		def result = createExpandedRows (instruction)
	}
	
	static void main(Object[] args) {
		JUnitCore.main(InstructionUtilsTests.name)
	}
	
}
