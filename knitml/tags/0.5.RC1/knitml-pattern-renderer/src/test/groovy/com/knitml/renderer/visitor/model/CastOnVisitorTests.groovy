package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.header.Yarn
import com.knitml.core.model.directions.block.Row
import com.knitml.core.model.directions.block.CastOn

import test.support.AbstractRenderingContextTests

class CastOnVisitorTests extends AbstractRenderingContextTests {

	
	@Test
	void castOnIdentifiedKey() {
		processXml '<cast-on style="long-tail" xmlns="http://www.knitml.com/schema/pattern">5</cast-on>', CastOn
		assertThat output, startsWith ('Using the long-tail method, cast on 5')
	}

	@Test
	void castOnUnidentifiedKey() {
		processXml '<cast-on style="filbert" xmlns="http://www.knitml.com/schema/pattern">5</cast-on>', CastOn
		assertThat output, startsWith ('Using the filbert method, cast on 5')
	}
	
	static void main(args) {
		JUnitCore.main(CastOnVisitorTests.name)
	}
	
}
