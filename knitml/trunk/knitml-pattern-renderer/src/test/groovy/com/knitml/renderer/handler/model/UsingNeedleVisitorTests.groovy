package com.knitml.renderer.handler.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringEndsWith.endsWith
import static org.junit.Assert.assertThat

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.common.NeedleStyle
import com.knitml.core.model.common.Needle;
import com.knitml.core.model.common.NeedleType;
import com.knitml.core.model.operations.block.Instruction;
import com.knitml.core.model.operations.block.Row;

import test.support.AbstractRenderingContextTests

class UsingNeedleVisitorTests extends AbstractRenderingContextTests {
	
	private static final String LINE_BREAK = System.getProperty("line.separator")
	
	@Test
	void basicUsingNeedle() {
		processXml PATTERN_START_TAG + '''
			<pattern:supplies>
				<pattern:yarn-types/>
				<pattern:needle-types>
					<pattern:needle-type id="needle-type1" type="circular">
						<pattern:needles>
							<common:needle id="needle1" label="Needle 1"/>
							<common:needle id="needle2" label="Needle 2"/>
						</pattern:needles>
					</pattern:needle-type>
				</pattern:needle-types>
				<pattern:accessories/>
			</pattern:supplies>
			<pattern:directions>
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
           	</pattern:directions>
           </pattern:pattern>
		'''
		assertThat output.trim(), endsWith ('Row 1: ' + LINE_BREAK + '    Needle 1: k5' + LINE_BREAK + '    Needle 2: p5')
	}
	
}
