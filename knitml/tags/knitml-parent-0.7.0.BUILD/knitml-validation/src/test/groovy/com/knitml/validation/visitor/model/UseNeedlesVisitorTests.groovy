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
			processXml PATTERN_START_TAG + '''
				<pattern:supplies>
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
					<pattern:instruction-group id="thing1">
						<use-needles>
							<needle ref="needle1"/>
						</use-needles>
						<cast-on>20</cast-on>
						<use-needles>
							<needle ref="needle1"/>
							<needle ref="needle2"/>
						</use-needles>
					</pattern:instruction-group>
            	</pattern:directions>
            </pattern:pattern>

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
		processXml PATTERN_START_TAG + '''
				<pattern:supplies>
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
					<row>
            			<knit/>
            		</row>
            	</pattern:directions>
            </pattern:pattern>
            '''
	}

}
