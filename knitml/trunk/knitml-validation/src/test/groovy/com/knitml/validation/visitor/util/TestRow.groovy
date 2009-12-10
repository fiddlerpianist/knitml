package com.knitml.validation.visitor.util

import com.knitml.core.model.directions.block.Row

class TestRow extends Row {
	
	TestRow (List<Integer> rowNumbers) {
		this.numbers = rowNumbers
	}
	
	String JiBX_className() {
		return "com.knitml.validation.visitor.util.TestRow"
	}
	
}