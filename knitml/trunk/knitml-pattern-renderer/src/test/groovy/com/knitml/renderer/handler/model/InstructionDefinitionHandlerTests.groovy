package com.knitml.renderer.handler.model

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.hamcrest.text.StringContains.containsString

import org.junit.Test
import org.junit.Before
import org.junit.runner.JUnitCore

import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.operations.block.Instruction;
import com.knitml.core.model.operations.block.Row;

import test.support.AbstractRenderingContextTests

class InstructionDefinitionHandlerTests extends AbstractRenderingContextTests {
	
	private static final String LINE_BREAK = System.getProperty("line.separator")
	
	@Test
	void inlineInstructionDefinition() {
		processXml PATTERN_START_TAG + '''
				<pattern:directives>
					<pattern:instruction-definitions>
						<inline-instruction id="cross-2-over-2" label="2/2 LC">
							<cross-stitches first="2" next="2" type="front" />
							<knit>4</knit>
						</inline-instruction>
					</pattern:instruction-definitions>
				</pattern:directives>
				<pattern:directions/>
			</pattern:pattern>'''
		assertThat output, startsWith ('2/2 LC: cross next 2 stitches in front of following 2 stitches, k4')
	}
	
	@Test
	void instructionDefinition() {
		processXml PATTERN_START_TAG + '''
				<pattern:directives>
					<pattern:instruction-definitions>
						<instruction id="sunny-day" label="Sunny Day Stitch" shape="round">
							<row>
								<knit>9</knit>
								<purl>1</purl>
							</row>
							<row>
								<knit>1</knit>
								<purl>9</purl>
							</row>
						</instruction>
					</pattern:instruction-definitions>
				</pattern:directives>
				<pattern:directions/>
			</pattern:pattern>'''
		assertThat output, startsWith ('Sunny Day Stitch:' + LINE_BREAK + 'Row 1: k9, p1' + LINE_BREAK + 'Row 2: k1, p9')
	}
	
	@Test
	void instructionDefinitionWithReference() {
		processXml PATTERN_START_TAG + '''
				<pattern:directives>
					<pattern:instruction-definitions>
						<instruction id="sunny-day" label="Sunny Day Stitch" shape="round">
							<row>
								<knit>9</knit>
								<purl>1</purl>
							</row>
							<row>
								<knit>1</knit>
								<purl>9</purl>
							</row>
						</instruction>
					</pattern:instruction-definitions>
				</pattern:directives>
				<pattern:directions>
					<cast-on>20</cast-on>
					<instruction id="next">
					  <for-each-row-in-instruction ref="sunny-day">
						<knit>5</knit>
						<apply-next-row instruction-ref="sunny-day"/>
						<knit>5</knit>
					  </for-each-row-in-instruction>
					</instruction>
				</pattern:directions>
			</pattern:pattern>'''
		assertThat output, containsString ('k5, work next row from Sunny Day Stitch instruction, k5')
	}
	
	@Test
	void mergedInstructionDefinition() {
		processXml PATTERN_START_TAG + '''
		  <pattern:directives>
		    <pattern:instruction-definitions>
		      <instruction id="left-side" label="Blah 1" shape="round">
		        <row>
		          <purl>2</purl>
		          <increase type="yo" />
		          <knit>3</knit>
		          <decrease type="ssk" />
		          <knit>9</knit>
		          <purl>1</purl>
		        </row>
		      </instruction>
		      <instruction id="right-side" label="Blah 2" shape="round">
		        <row>
		          <purl>1</purl>
		          <knit>9</knit>
		          <decrease type="k2tog" />
		          <knit>3</knit>
		          <increase type="yo" />
		          <purl>2</purl>
		        </row>
		      </instruction>
		      <merged-instruction id="merged" merge-point="row" type="physical" label="Merged Instruction">
		        <instruction-ref ref="left-side" />
		        <instruction-ref ref="right-side" />
		      </merged-instruction>
		   </pattern:instruction-definitions>
		 </pattern:directives>
		<pattern:directions/>
		</pattern:pattern>
		'''
		assertThat output, containsString ('Merged Instruction:' + LINE_BREAK + 'Row 1: p2, yo, k3, ssk, k9, p2, k9, k2tog, k3, yo, p2')
	}

	@Test
	void mergedInstructionDefinitionWithReference() {
		processXml PATTERN_START_TAG + '''
		  <pattern:directives>
		    <pattern:instruction-definitions>
		      <instruction id="left-side" label="Blah 1" shape="round">
		        <row>
		          <purl>2</purl>
		          <increase type="yo" />
		          <knit>3</knit>
		          <decrease type="ssk" />
		          <knit>9</knit>
		          <purl>1</purl>
		        </row>
		      </instruction>
		      <instruction id="right-side" label="Blah 2" shape="round">
		        <row>
		          <purl>1</purl>
		          <knit>9</knit>
		          <decrease type="k2tog" />
		          <knit>3</knit>
		          <increase type="yo" />
		          <purl>2</purl>
		        </row>
		      </instruction>
		      <merged-instruction id="merged" merge-point="row" type="physical" label="Merged Instruction">
		        <instruction-ref ref="left-side" />
		        <instruction-ref ref="right-side" />
		      </merged-instruction>
		   </pattern:instruction-definitions>
		 </pattern:directives>
		<pattern:directions>
			<cast-on>44</cast-on>
			<join-in-round/>
			<instruction id="next">
			  <for-each-row-in-instruction ref="merged">
				<knit>5</knit>
				<apply-next-row instruction-ref="merged"/>
				<knit>5</knit>
			  </for-each-row-in-instruction>
			</instruction>
		</pattern:directions>
		</pattern:pattern>
		'''
		assertThat output, containsString ('k5, work next row from Merged Instruction instruction, k5')
	}
	
}
