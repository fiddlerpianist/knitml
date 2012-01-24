package com.knitml.renderer.impl.charting.analyzer

import static com.knitml.core.model.directions.inline.Repeat.Until.TIMES
import static com.knitml.renderer.context.ContextUtils.deriveInstructionInfo
import static test.support.AbstractRenderingContextTests.PATTERN_START_TAG
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue
import static test.support.JiBXUtils.parseXml

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

import com.knitml.core.model.Pattern
import com.knitml.core.model.directions.block.Instruction
import com.knitml.renderer.context.InstructionInfo
import com.knitml.renderer.context.Options
import com.knitml.renderer.context.RenderingContext
import com.knitml.validation.context.impl.DefaultKnittingContextFactory

@RunWith(BlockJUnit4ClassRunner.class)
public class ChartingAnalyzerBodyTests {
	
	protected RenderingContext context
	protected ChartingAnalyzer analyzer
	
	@Before
	public void setUp() {
		context = new RenderingContext(new Options())
		context.knittingContext = new DefaultKnittingContextFactory().createKnittingContext()
		analyzer = new ChartingAnalyzer(context)
	}
	
	@Test
	public void simpleInstruction() {
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns="http://www.knitml.com/schema/operations"> 
			<row> 
				<knit>2</knit>
				<purl>2</purl>
			</row>
        	<row>
            	<knit>4</knit>
			</row>
		 </instruction>''', Instruction)
		context.engine.castOn 4
		// the 'false' indicates that we do not want to do a dynamic cast-on
		Analysis analysis = analyzer.analyzeInstruction(instruction,null,false)
		assertTrue analysis.isChartable()
		assertThat analysis.maxWidth, is (4)
	}
	
	
	@Test
	public void simpleInstructionWithExplicitRepeat() {
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/operations'> 
			<row> 
				<repeat until='times' value='4'>
		         <knit>2</knit>
		         <purl>2</purl> 
		       </repeat>
		    </row>
		 </instruction>''', Instruction)
		context.engine.castOn 16
		Analysis analysis = analyzer.analyzeInstruction(instruction,null,false)
		assertTrue analysis.isChartable()
		assertThat analysis.maxWidth, is (16)
	}
	
	@Test
	public void simpleInstructionWithContextualRepeat() {
		context.engine.castOn 16
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/operations'> 
			<row> 
				<repeat until='end'>
		         <knit>2</knit>
		         <purl>2</purl> 
		       </repeat>
		    </row>
		 </instruction>''', Instruction)
		Analysis analysis = analyzer.analyzeInstruction(instruction,null,false)
		assertTrue analysis.isChartable()
		assertThat analysis.maxWidth, is (16)
		assertThat analysis.instructionToUse, not (null)
		// ensure that the second row's repeat has been literalized to '12 times'
		analysis.instructionToUse.rows[0].operations[0].with {
			assertThat until, is (TIMES)
			assertThat value, is (4)
		}
	}
	
	@Test
	public void instructionWithTurn() {
		// ensure not processable
		context.engine.castOn 4
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/operations'> 
			<row> 
		         <knit>2</knit>
		         <turn/>
		         <purl>2</purl>
		         <turn/>
		         <knit>4</knit> 
		    </row>
		 </instruction>''', Instruction)
		Analysis analysis = analyzer.analyzeInstruction(instruction,null,false)
		assertFalse analysis.isChartable()
	}
	
	@Test
	public void instructionWithApplyNextRow() {
		context.engine.castOn 4
		Pattern pattern = parseXml ('''
		<pattern:pattern xmlns:pattern='http://www.knitml.com/schema/pattern' xmlns='http://www.knitml.com/schema/operations'>
			<pattern:directions>
				<instruction id='ref-inst'>
					<row>
						<knit>4</knit>
					</row>
				</instruction>
				<instruction id='inst1'>  
					<row>
						<apply-next-row instruction-ref="ref-inst"/>
		         	</row>
		         </instruction>
		    </pattern:directions>
		 </pattern:pattern>''', Pattern)
		InstructionInfo instructionInfo = deriveInstructionInfo(pattern.directions.operations[0], "Ref Instruction")
		context.patternRepository.addGlobalInstruction(instructionInfo)
		Analysis analysis = analyzer.analyzeInstruction(pattern.directions.operations[1],null,false)
		assertFalse analysis.isChartable()
	}
	
	@Test
	public void instructionWithBindOffAll() {
		// ensure not processable
		context.engine.castOn 4
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/operations'> 
			<row>
				<bind-off-all/>
		    </row>
		 </instruction>''', Instruction)
		Analysis analysis = analyzer.analyzeInstruction(instruction,null,false)
		assertFalse analysis.isChartable()
	}
	
	@Test
	public void instructionWithDesignateEndOfRow() {
		context.engine.castOn 4
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/operations'> 
			<row>
				<designate-end-of-row/>
		    </row>
		 </instruction>''', Instruction)
		Analysis analysis = analyzer.analyzeInstruction(instruction,null,false)
		assertFalse analysis.isChartable()
	}
	
	@Test
	public void chartableInstructionWithNewMarker() {
		context.engine.castOn 12
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/operations'> 
			<row>
				<knit>6</knit>
				<place-marker/>
				<knit>6</knit>
		    </row>
		    <row>
		    	<repeat until="marker">
		    		<knit/>
		    	</repeat>
		    	<repeat until="end">
		    		<purl/>
		    	</repeat>
		    </row>
		 </instruction>''', Instruction)
		Analysis analysis = analyzer.analyzeInstruction(instruction,null,false)
		assertTrue analysis.isChartable()
		assertThat analysis.maxWidth, is (12)
		analysis.instructionToUse.rows[1].operations[0].with {
			assertThat until, is (TIMES)
			assertThat value, is (6)
		}
		analysis.instructionToUse.rows[1].operations[1].with {
			assertThat until, is (TIMES)
			assertThat value, is (6)
		}
	}
	
	@Test
	void chartableInstructionWithExistingMarker() {
		context.engine.with {
			castOn 12
			startNewRow()
			slip 6
			placeMarker()
			slip 6
			endRow()
		}
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/operations'> 
		    <row>
		    	<repeat until="marker">
		    		<knit/>
		    	</repeat>
		    	<repeat until="end">
		    		<purl/>
		    	</repeat>
		    </row>
		 </instruction>''', Instruction)
		Analysis analysis = analyzer.analyzeInstruction(instruction,null,false)
		assertTrue analysis.isChartable()
		assertThat analysis.maxWidth, is (12)
		analysis.instructionToUse.rows[0].operations[0].with {
			assertThat until, is (TIMES)
			assertThat value, is (6)
		}
	}
	
	@Test
	public void instructionWithUsingNeedle() {
		context.engine.castOn 4
		Pattern pattern = parseXml (PATTERN_START_TAG + 
'''		  <pattern:supplies>
			<pattern:yarn-types/>
			<pattern:needle-types>
				<pattern:needle-type id="needle-type1" type="circular">
					<pattern:needles> 
						<common:needle id="needle1"/>
						<common:needle id="needle2"/>
					</pattern:needles> 
				</pattern:needle-type>
			</pattern:needle-types>
			<pattern:accessories/>
		  </pattern:supplies>
		<pattern:directions>
		 <instruction id="inst1">
		  <row>
		  	<using-needle ref="needle1">
		  		<knit>4</knit>
		  	</using-needle>
		  </row>
		 </instruction>
    	</pattern:directions>
        </pattern:pattern>''', Pattern)
		Analysis analysis = analyzer.analyzeInstruction(pattern.directions.operations[0],null,false)
		assertFalse analysis.isChartable()
	}

	@Test
	public void forEachRowInInstruction() {
		context.engine.castOn 4
		Pattern pattern = parseXml ('''
		<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations">
		<pattern:directions>
		 <instruction id="inst1">
		  <row>
		  	<knit>4</knit>
		  </row>
		 </instruction>
		 <instruction id="inst2">
		 	<for-each-row-in-instruction ref="inst1">
		 		<knit>2</knit>
	 			<purl>2</purl>
		 	</for-each-row-in-instruction>
		 </instruction>
    	</pattern:directions>
        </pattern:pattern>''', Pattern)
		Analysis analysis = analyzer.analyzeInstruction(pattern.directions.operations[1],null,false)
		assertFalse analysis.isChartable()
	}
	
	
	@Test
	public void instructionWithNoStitch() {
		context.engine.castOn 4
		Instruction instruction = parseXml ('''
				<instruction id='inst1' xmlns="http://www.knitml.com/schema/operations"> 
					<row> 
						<knit>2</knit>
						<no-stitch>4</no-stitch>
						<purl>2</purl>
					</row>
				 </instruction>''', Instruction)
		Analysis analysis = analyzer.analyzeInstruction(instruction,null,false)
		assertTrue analysis.isChartable()
		assertThat analysis.maxWidth, is (8)
	}
}
