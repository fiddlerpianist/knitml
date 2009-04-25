package com.knitml.renderer.impl.charting.analyzer

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertThat
import static test.support.JiBXUtils.parseXml
import com.knitml.renderer.context.Optionsimport com.knitml.validation.context.impl.DefaultKnittingContextFactory
import java.io.StringReader

import org.junit.Before
import org.junit.Test
import org.junit.internal.runners.JUnit4ClassRunner
import org.junit.runner.RunWith

import com.knitml.core.model.directions.inline.Repeat.Until
import com.knitml.core.model.directions.block.Instruction
import com.knitml.renderer.context.RenderingContext
import com.knitml.renderer.Renderer

@RunWith(JUnit4ClassRunner.class)
public class ChartingAnalyzerHeaderTests {
	
	protected RenderingContext context
	protected ChartingAnalyzer analyzer
	
	@Before
	public void setUp() {
		context = new RenderingContext(new Options())
		context.knittingContext = new DefaultKnittingContextFactory().createKnittingContext()
		analyzer = new ChartingAnalyzer(context)
	}

	@Test
	public void simpleHeaderInstruction() throws Exception {
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/pattern'> 
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
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/pattern'> 
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
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/pattern'> 
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
