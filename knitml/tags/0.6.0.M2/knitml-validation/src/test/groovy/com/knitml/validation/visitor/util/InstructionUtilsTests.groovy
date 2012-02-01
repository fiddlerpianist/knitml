package com.knitml.validation.visitor.util

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static com.knitml.validation.visitor.util.InstructionUtils.createExpandedRows
import static com.knitml.validation.visitor.util.InstructionUtils.areRowsConsecutive

import java.util.List

import org.junit.Test
import org.junit.runner.RunWith

import com.knitml.core.model.directions.block.Instruction
import com.knitml.validation.common.InvalidStructureException

import static com.knitml.core.common.RowDefinitionScope.ODD
import static com.knitml.core.common.RowDefinitionScope.EVEN

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
		Instruction instruction = new Instruction("thingy1",null,null,null,rows)
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
	void processableInstructionWithSubsequentRowsTypicalDefinition() {
		TestRow first = new TestRow([1], ODD)
		TestRow second = new TestRow([2], EVEN)
		List rows = [first,second]
		Instruction instruction = new Instruction("thingy1",null,null,null,rows,8)
		def result = createExpandedRows (instruction)
		assertThat result.rows.size(), is (8)
		assertThat result.rows[0], is (first)
		assertThat result.rows[1], is (second)
		assertThat result.rows[2], is (first)
		assertThat result.rows[3], is (second)
		assertThat result.rows[4], is (first)
		assertThat result.rows[5], is (second)
		assertThat result.rows[6], is (first)
		assertThat result.rows[7], is (second)
	}
	
	@Test
	void processableInstructionWithSubsquentRowsUnusualDefinition() {
		TestRow first = new TestRow([1,2])
		TestRow second = new TestRow([3,5], EVEN)
		TestRow third = new TestRow([4,7], ODD)
		List rows = [first,second,third]
		Instruction instruction = new Instruction("thingy1",null,null,null,rows,9)
		def result = createExpandedRows (instruction)
		assertThat result.rows.size(), is (9)
		assertThat result.rows[0], is (first)
		assertThat result.rows[1], is (first)
		assertThat result.rows[2], is (second)
		assertThat result.rows[3], is (third)
		assertThat result.rows[4], is (second)
		assertThat result.rows[5], is (second)
		assertThat result.rows[6], is (third)
		assertThat result.rows[7], is (second)
		assertThat result.rows[8], is (third)
	}
	
	@Test
	void processableInstructionWithNoRowNumbers() {
		TestRow first = new TestRow(null)
		TestRow second = new TestRow(null)
		TestRow third = new TestRow(null)
		List rows = [first,second,third]
		Instruction instruction = new Instruction("thingy1",null,null,null,rows)
		def result = createExpandedRows (instruction)
		assertThat result.rows[0], is (first)
		assertThat result.rows[1], is (second)
		assertThat result.rows[2], is (third)
	}
	
	@Test(expected=InvalidStructureException)
	void notProcessableInstructionIncompleteDefinition() {
		TestRow first = new TestRow([1,3,5])
		List rows = [first]
		Instruction instruction = new Instruction("thingy1",null,null,null,rows)
		def result = createExpandedRows (instruction)
	}
	
	@Test(expected=InvalidStructureException)
	void notProcessableInstructionAmbiguousDefinition() {
		TestRow first = new TestRow([1,2])
		TestRow second = new TestRow([1])
		List rows = [first,second]
		Instruction instruction = new Instruction("thingy1",null,null,null,rows)
		def result = createExpandedRows (instruction)
	}
	
	@Test(expected=InvalidStructureException)
	void notProcessableInstructionRowCountValidation() {
		TestRow first = new TestRow([1,2])
		List rows = [first]
		Instruction instruction = new Instruction("thingy1",null,null,null,rows,3)
		def result = createExpandedRows (instruction)
	}

	@Test(expected=InvalidStructureException)
	void notProcessableInstructionSubsequentWithNoRowCount() {
		TestRow first = new TestRow([1], ODD)
		TestRow second = new TestRow([2], EVEN)
		List rows = [first,second]
		Instruction instruction = new Instruction("thingy1",null,null,null,rows)
		def result = createExpandedRows (instruction)
	}
	

	
}
