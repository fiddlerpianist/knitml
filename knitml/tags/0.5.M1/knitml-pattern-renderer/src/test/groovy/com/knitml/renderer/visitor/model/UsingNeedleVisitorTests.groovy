package com.knitml.renderer.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringEndsWith.endsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.common.NeedleStyle
import com.knitml.core.model.header.NeedleType
import com.knitml.core.model.header.Needle
import com.knitml.core.model.directions.block.Row
import com.knitml.core.model.directions.block.Instruction

import test.support.AbstractRenderingContextTests

class UsingNeedleVisitorTests extends AbstractRenderingContextTests {
	
	private static final String LINE_BREAK = System.getProperty("line.separator")
	
	@Test
	void basicUsingNeedle() {
		processXml '''
		<pattern xmlns="http://www.knitml.com/schema/pattern">
			<supplies>
				<yarns/>
				<needles>
					<needle-type id="needle-type1" type="circular"/> 
					<needle id="needle1" typeref="needle-type1" label="Needle 1"/>
					<needle id="needle2" typeref="needle-type1" label="Needle 2"/>
				</needles>
				<accessories/>
			</supplies>
			<directions>
				<use-needles>
					<needle ref="needle1"/>
				</use-needles>
				<cast-on>10</cast-on>
				<use-needles>
					<needle ref="needle1"/>
					<needle ref="needle2"/>
				</use-needles>
				<arrange-stitches-on-needles>
					<needle ref="needle1">5</needle>
					<needle ref="needle2">5</needle>
				</arrange-stitches-on-needles>
				<row number="1">
					<using-needle ref="needle1">
						<knit>5</knit>
					</using-needle>
					<using-needle ref="needle2">
						<purl>5</purl>
					</using-needle>
				</row>
           	</directions>
           </pattern>
		'''
		assertThat output.trim(), endsWith ('Row 1: ' + LINE_BREAK + '    Needle 1: k5' + LINE_BREAK + '    Needle 2: p5')
	}
	
	static void main(args) {
		JUnitCore.main(UsingNeedleVisitorTests.name)
	}
	
}
