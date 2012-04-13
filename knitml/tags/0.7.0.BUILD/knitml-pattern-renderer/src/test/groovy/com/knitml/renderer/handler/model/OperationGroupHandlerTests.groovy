package com.knitml.renderer.handler.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.junit.Assert.assertThat

import org.junit.Before
import org.junit.Test

import test.support.AbstractRenderingContextTests

import com.knitml.core.model.operations.block.Row
import com.knitml.core.model.operations.inline.OperationGroup

class OperationGroupHandlerTests extends AbstractRenderingContextTests {

	@Before
	void setUpContext() {
		renderingContext.with {
			engine.castOn 5
		}
	}
	
	@Test
	void outsideOfRow() {
		renderingContext.engine.startNewRow()
		processXml '''
<group xmlns="http://www.knitml.com/schema/operations" size="5">
		<knit>3</knit>
		<purl>2</purl>
</group>''', OperationGroup
		assertThat output, is ('k3, p2')
	}

	@Test
	void withinRow() {
		processXml '''
<row xmlns="http://www.knitml.com/schema/operations" number="1">
		<group size="5">
			<knit>3</knit>
			<purl>2</purl>
		</group>
</row>''', Row
		assertThat output.trim(), is ('Row 1: k3, p2')
	}
	
}
