package com.knitml.validation.visitor.model

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue

import java.util.ArrayList
import java.util.List

import org.jibx.runtime.JiBXException
import org.junit.Test
import org.junit.runner.JUnitCore

import test.support.AbstractKnittingContextTests

import com.knitml.engine.Needle
import com.knitml.core.common.NeedleStyle
import com.knitml.validation.visitor.NeedleNotFoundException
import com.knitml.engine.common.NoActiveNeedlesException

class UseNeedlesVisitorTests extends AbstractKnittingContextTests {
	
	@Test
	void useTwoNeedles() {
		engine.with {
			processXml '''
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
					<instruction-group id="thing1">
						<use-needles>
							<needle ref="needle1"/>
						</use-needles>
						<cast-on>20</cast-on>
						<use-needles>
							<needle ref="needle1"/>
							<needle ref="needle2"/>
						</use-needles>
					</instruction-group>
            	</directions>
            </pattern>

			'''
			assertThat numberOfNeedles, is(2)
			int[] newStitchArrangement = [10, 10] 
			arrangeStitchesOnNeedles newStitchArrangement
			startNewRow()
			assertThat totalNumberOfStitchesOnCurrentNeedle, is (10)
			knit 10
			advanceNeedle()
			assertThat totalNumberOfStitchesOnCurrentNeedle, is (10)
			knit 10
			endRow()
		}
	}
	
	@Test(expected=JiBXException)
	void useNeedleNotInRepository() {
		processXml '''
	<instruction id="thing1">
		<use-needles>
			<needle ref="needle3"/>
		</use-needles>
	</instruction>
		'''
	}
	
	@Test(expected=NoActiveNeedlesException)
	void doNotSpecifyAnyNeedles() {
			processXml '''
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
					<row>
            			<knit/>
            		</row>
            	</directions>
            </pattern>
            '''
	}

	static void main(args) {
		JUnitCore.main(UseNeedlesVisitorTests.name)
	}
	
}
