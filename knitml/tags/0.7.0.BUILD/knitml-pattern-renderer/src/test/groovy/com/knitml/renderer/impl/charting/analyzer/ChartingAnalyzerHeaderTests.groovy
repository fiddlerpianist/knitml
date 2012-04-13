package com.knitml.renderer.impl.charting.analyzer

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertThat
import static test.support.JiBXUtils.parseXml

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

import com.knitml.core.model.operations.inline.Repeat.Until
import com.knitml.core.model.operations.block.Instruction;
import com.knitml.renderer.context.Options
import com.knitml.renderer.context.RenderingContext
import com.knitml.validation.context.impl.DefaultKnittingContextFactory

@RunWith(BlockJUnit4ClassRunner.class)
public class ChartingAnalyzerHeaderTests {
	
	protected RenderingContext context
	protected ChartingAnalyzer analyzer
	
	@Before
	public void setUp() {
		context = new RenderingContext(new Options())
		context.knittingContext = new DefaultKnittingContextFactory().create()
		analyzer = new ChartingAnalyzer(context)
	}

	@Test
	public void simpleHeaderInstruction() throws Exception {
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/operations'> 
			<row> 
				<knit>2</knit>
				<purl>2</purl>
			</row>
        	<row>
            	<knit>4</knit>
			</row>
		 </instruction>''', Instruction)
		Analysis analysis = analyzer.analyzeInstruction(instruction,null,true)
		assertThat analysis.chartable, is (true)
		assertThat analysis.maxWidth, is (4)
	}

	
	@Test
	public void headerInstructionWithExplicitAndContextualRepeat() throws Exception {
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/operations'> 
			<row> 
				<repeat until='times' value='3'>
		         <knit>2</knit>
		         <purl>2</purl> 
		       </repeat>
		    </row>
        	<row>
            	<repeat until='end'><knit/></repeat>
			</row>
		 </instruction>''', Instruction)
		Analysis analysis = analyzer.analyzeInstruction(instruction,null,true)
		assertThat analysis.chartable, is (true)
		assertThat analysis.maxWidth, is (12)
		// ensure that the second row's repeat has been literalized to '12 times'
		analysis.instructionToUse.rows[1].operations[0].with {
			assertThat until, is (Until.TIMES)
			assertThat value, is (12)
		}
	}
	
	@Test
	public void headerInstructionWithFirstRowContextualRepeat() throws Exception {
		Instruction instruction = parseXml (''' 
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/operations'> 
			<row>
				<repeat until='end'>
		         <knit>2</knit>
		         <purl>2</purl>
		       </repeat>
			</row>
		 </instruction>''', Instruction)
		Analysis analysis = analyzer.analyzeInstruction(instruction,null,true)
		assertThat analysis.chartable, is (false)
	}
}
