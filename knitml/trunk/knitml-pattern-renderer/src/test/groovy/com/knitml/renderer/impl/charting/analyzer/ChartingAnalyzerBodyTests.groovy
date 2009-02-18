package com.knitml.renderer.impl.charting.analyzer

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertThat
import static com.knitml.core.model.directions.inline.Repeat.Until.TIMES

import java.io.StringReader

import org.jibx.runtime.BindingDirectory
import org.jibx.runtime.IBindingFactory
import org.jibx.runtime.IUnmarshallingContext
import org.junit.Before
import org.junit.Test
import org.junit.internal.runners.JUnit4ClassRunner
import org.junit.runner.RunWith

import com.knitml.core.model.directions.block.Instruction
import com.knitml.core.model.Pattern
import com.knitml.renderer.context.RenderingContext
import com.knitml.renderer.context.RenderingContextFactory
import com.knitml.renderer.context.impl.DefaultRenderingContextFactory

@RunWith(JUnit4ClassRunner.class)
public class ChartingAnalyzerBodyTests {
	
	protected RenderingContext context
	protected ChartingAnalyzer analyzer
	
	@Before
	public void setUp() {
		RenderingContextFactory factory = new DefaultRenderingContextFactory()
		context = factory.createRenderingContext()
		// the 'false' indicates that we do not want to do a dynamic cast-on
		analyzer = new ChartingAnalyzer(context, false)
	}
	
	protected <C> C parseXml(String xml, Class<C> rootClass) throws Exception {
		IBindingFactory factory = BindingDirectory.getFactory(rootClass)
		IUnmarshallingContext uctx = factory.createUnmarshallingContext()
		return (C) uctx.unmarshalDocument(new StringReader(xml))
	}
	
	@Test
	public void simpleInstruction() {
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns="http://www.knitml.com/schema/pattern"> 
			<row> 
				<knit>2</knit>
				<purl>2</purl>
			</row>
        	<row>
            	<knit>4</knit>
			</row>
		 </instruction>''', Instruction)
		context.engine.castOn 4
		Instruction newInstruction = analyzer.analyzeInstruction(instruction)
		assertNotNull(newInstruction)
		assertThat analyzer.maxWidth, is (4)
	}
	
	
	@Test
	public void simpleInstructionWithExplicitRepeat() {
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/pattern'> 
			<row> 
				<repeat until='times' value='4'>
		         <knit>2</knit>
		         <purl>2</purl> 
		       </repeat>
		    </row>
		 </instruction>''', Instruction)
		context.engine.castOn 16
		Instruction newInstruction = analyzer.analyzeInstruction(instruction)
		assertThat newInstruction, not (null)
		assertThat analyzer.maxWidth, is (16)
	}
	
	@Test
	public void simpleInstructionWithContextualRepeat() {
		context.engine.castOn 16
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/pattern'> 
			<row> 
				<repeat until='end'>
		         <knit>2</knit>
		         <purl>2</purl> 
		       </repeat>
		    </row>
		 </instruction>''', Instruction)
		Instruction newInstruction = analyzer.analyzeInstruction(instruction)
		assertThat newInstruction, not (null)
		assertThat analyzer.maxWidth, is (16)
		// ensure that the second row's repeat has been literalized to '12 times'
		newInstruction.rows[0].operations[0].with {
			assertThat until, is (TIMES)
			assertThat value, is (4)
		}
	}
	
	@Test
	public void instructionWithTurn() {
		// ensure not processable
		context.engine.castOn 4
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/pattern'> 
			<row> 
		         <knit>2</knit>
		         <turn/>
		         <purl>2</purl>
		         <turn/>
		         <knit>4</knit> 
		    </row>
		 </instruction>''', Instruction)
		Instruction newInstruction = analyzer.analyzeInstruction(instruction)
		assertThat newInstruction, is (null)
	}
	
	@Test
	public void instructionWithApplyNextRow() {
		context.engine.castOn 4
		Pattern pattern = parseXml ('''
		<pattern xmlns='http://www.knitml.com/schema/pattern'>
			<directions>
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
		    </directions>
		 </pattern>''', Pattern)
		context.patternRepository.addGlobalInstruction(pattern.directions.operations[0], "Ref Instruction")
		Instruction newInstruction = analyzer.analyzeInstruction(pattern.directions.operations[1])
		assertThat newInstruction, is (null)
	}
	
	@Test
	public void instructionWithBindOffAll() {
		// ensure not processable
		context.engine.castOn 4
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/pattern'> 
			<row>
				<bind-off-all/>
		    </row>
		 </instruction>''', Instruction)
		Instruction newInstruction = analyzer.analyzeInstruction(instruction)
		assertThat newInstruction, is (null)
	}
	
	@Test
	public void instructionWithDesignateEndOfRow() {
		context.engine.castOn 4
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/pattern'> 
			<row>
				<designate-end-of-row/>
		    </row>
		 </instruction>''', Instruction)
		Instruction newInstruction = analyzer.analyzeInstruction(instruction)
		assertThat newInstruction, is (null)
	}
	
	@Test
	public void chartableInstructionWithNewMarker() {
		context.engine.castOn 12
		Instruction instruction = parseXml ('''
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/pattern'> 
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
		Instruction newInstruction = analyzer.analyzeInstruction(instruction)
		assertThat newInstruction, not (null)
		assertThat analyzer.maxWidth, is (12)
		newInstruction.rows[1].operations[0].with {
			assertThat until, is (TIMES)
			assertThat value, is (6)
		}
		newInstruction.rows[1].operations[1].with {
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
		<instruction id='inst1' xmlns='http://www.knitml.com/schema/pattern'> 
		    <row>
		    	<repeat until="marker">
		    		<knit/>
		    	</repeat>
		    	<repeat until="end">
		    		<purl/>
		    	</repeat>
		    </row>
		 </instruction>''', Instruction)
		Instruction newInstruction = analyzer.analyzeInstruction(instruction)
		assertThat newInstruction, not (null)
		assertThat analyzer.maxWidth, is (12)
		newInstruction.rows[0].operations[0].with {
			assertThat until, is (TIMES)
			assertThat value, is (6)
		}
	}
	
	@Test
	public void instructionWithUsingNeedle() {
		context.engine.castOn 4
		Pattern pattern = parseXml ('''
		<pattern xmlns="http://www.knitml.com/schema/pattern">
		  <supplies>
			<yarns/>
			<needles>
				<needle-type id="needle-type1" type="circular"/> 
				<needle id="needle1" typeref="needle-type1"/>
				<needle id="needle2" typeref="needle-type1"/>
			</needles>
			<accessories/>
		  </supplies>
		<directions>
		 <instruction id="inst1">
		  <row>
		  	<using-needle ref="needle1">
		  		<knit>4</knit>
		  	</using-needle>
		  </row>
		 </instruction>
    	</directions>
        </pattern>''', Pattern)
		Instruction newInstruction = analyzer.analyzeInstruction(pattern.directions.operations[0])
		assertThat newInstruction, is (null)
	}

	@Test
	public void forEachRowInInstruction() {
		context.engine.castOn 4
		Pattern pattern = parseXml ('''
		<pattern xmlns="http://www.knitml.com/schema/pattern">
		<directions>
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
    	</directions>
        </pattern>''', Pattern)
		Instruction newInstruction = analyzer.analyzeInstruction(pattern.directions.operations[1])
		assertThat newInstruction, is (null)
	}
	
	
	@Test
	public void instructionWithNoStitch() {
		context.engine.castOn 4
		Instruction instruction = parseXml ('''
				<instruction id='inst1' xmlns="http://www.knitml.com/schema/pattern"> 
					<row> 
						<knit>2</knit>
						<no-stitch>4</no-stitch>
						<purl>2</purl>
					</row>
				 </instruction>''', Instruction)
		Instruction newInstruction = analyzer.analyzeInstruction(instruction)
		assertNotNull(newInstruction)
		assertThat analyzer.maxWidth, is (8)
	}
}
