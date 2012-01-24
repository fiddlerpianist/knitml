package com.knitml.validation.visitor.model

import java.util.ArrayList
import java.util.List

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.assertThat

import org.junit.Test

import test.support.AbstractKnittingContextTests

import com.knitml.core.common.KnittingShape
import com.knitml.validation.common.InvalidStructureException
import com.knitml.engine.common.UnexpectedRowNumberException
import com.knitml.engine.common.KnittingEngineException
import com.knitml.engine.KnittingEngine
import com.knitml.engine.Needle

class ApplyNextRowVisitorTests extends AbstractKnittingContextTests {
	
	@Test
	void applyNextRowMultipleTimesWithinCurrentRow() {
		// after the first row, there should be 20 sts left
		// after the fourth row, there should be 8 sts left
		processXml PATTERN_START_TAG + '''
				<pattern:directives>
					<pattern:instruction-definitions>
						<instruction id="top-decrease" shape="flat" label="Top Decrease">
							<row>
								<knit>4</knit>
								<decrease type="k2tog"/>
							</row>
							<row>
								<purl>3</purl>
								<decrease type="p2tog"/>
							</row>
							<row>
								<knit>2</knit>
								<decrease type="k2tog"/>
							</row>
							<row>
								<purl>1</purl>
								<decrease type="p2tog"/>
							</row>
						</instruction>
					</pattern:instruction-definitions>
				</pattern:directives>
				<pattern:directions>
					<cast-on>24</cast-on>
					<instruction id="body-repeat">
						<row>
							<repeat until="times" value="4">
								<apply-next-row instruction-ref="top-decrease"/>
							</repeat>
						</row>
					</instruction>
					<information>
						<number-of-stitches	number="20"/>
					</information>
					<repeat-instruction ref="body-repeat">
						<additional-times>3</additional-times>
					</repeat-instruction>
					<information>
						<number-of-stitches	number="8"/>
					</information>
				</pattern:directions>
			</pattern:pattern>
			'''
	}


	@Test
	void applyNextRowMultipleTimesWithinCurrentRowUsingForEachRowInInstruction() {
		// after execution, there should be 8 sts left
		processXml PATTERN_START_TAG + '''
				<pattern:directives>
					<pattern:instruction-definitions>
						<instruction id="top-decrease" shape="flat" label="Top Decrease">
							<row>
								<knit>4</knit>
								<decrease type="k2tog"/>
							</row>
							<row>
								<purl>3</purl>
								<decrease type="p2tog"/>
							</row>
							<row>
								<knit>2</knit>
								<decrease type="k2tog"/>
							</row>
							<row>
								<purl>1</purl>
								<decrease type="p2tog"/>
							</row>
						</instruction>
					</pattern:instruction-definitions>
				</pattern:directives>
				<pattern:directions>
					<cast-on>24</cast-on>
					<instruction id="body-repeat">
						<for-each-row-in-instruction ref="top-decrease">
							<repeat until="times" value="4">
								<apply-next-row instruction-ref="top-decrease"/>
							</repeat>
						</for-each-row-in-instruction>
					</instruction>
					<information>
						<number-of-stitches	number="8"/>
					</information>
				</pattern:directions>
			</pattern:pattern>
			'''
	}

}
