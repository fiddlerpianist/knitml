package com.knitml.validation.visitor.util

import com.knitml.core.common.RowDefinitionScope 
import com.knitml.core.model.directions.block.Row

class TestRow extends Row {
	
	TestRow (List<Integer> rowNumbers) {
		this.numbers = rowNumbers
	}
	
	TestRow (List<Integer> rowNumbers, RowDefinitionScope subsequentRows) {
		this.numbers = rowNumbers
		this.subsequent = subsequentRows
	}
	
	String JiBX_className() {
		return "com.knitml.validation.visitor.util.TestRow"
	}
	
}