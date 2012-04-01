/**
 * 
 */
package com.knitml.transform.gauge

import static com.knitml.core.units.Units.STITCHES_PER_INCH
import static com.knitml.transform.gauge.Unmarshaller.unmarshal
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.assertThat

import org.junit.Before
import org.junit.Test

import com.knitml.core.model.operations.block.CastOn
import com.knitml.core.model.operations.block.Row
import com.knitml.core.model.pattern.Parameters
import com.knitml.core.model.pattern.Pattern
import com.knitml.core.model.pattern.Version
import com.knitml.transform.gauge.event.TransformHandler;
import com.knitml.transform.gauge.event.TransformPatternEventListener
import com.knitml.validation.ValidationProgram

/**
 * Each test must cast on 20 stitches and report a stitch total deviation with the 'validateChange' method.
 */
abstract class AbstractTransformerTests {
	
	public static final String PATTERN_START_TAG = "<pattern:pattern xmlns:pattern=\"http://www.knitml.com/schema/pattern\" xmlns:common=\"http://www.knitml.com/schema/common\" xmlns=\"http://www.knitml.com/schema/operations\" version=\"" + Version.getCurrentVersionId() + "\">" //$NON-NLS-1$ //$NON-NLS-2$
	
	private ValidationProgram program
	
	abstract void setUpGauges(TransformHandler handler)
	
	abstract void validate (Row row)
	
	abstract void validateChange (Row row, int delta)
	
	abstract void validate (CastOn castOn)
	
	@Before
	void setUp() {
		TransformHandler handler = new TransformHandler()
		setUpGauges(handler)
		program = new ValidationProgram(new TransformPatternEventListener(handler))
	}
	
	@Test
	void knitsPurls() {
		def xml = PATTERN_START_TAG + '''
			  <pattern:directions>
			  		<cast-on>20</cast-on>
					<row>
						<knit>10</knit>
						<purl>9</purl>
						<knit/>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshal (xml)
		transform pattern
		Row row = pattern.directions.operations[1]
		validate row
		validateChange row, 0
	}
	
	@Test
	void pickUpStitchesDecreaseAndTurn() {
		def xml = PATTERN_START_TAG + '''
			  <pattern:directions>
			  		<cast-on>20</cast-on>
					<row short="true">
						<inline-pick-up-stitches>10</inline-pick-up-stitches>
						<knit>10</knit>
						<decrease type="ssk"/>
						<knit>8</knit>
						<turn/>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshal (xml)
		transform pattern
		Row row = pattern.directions.operations[1]
		validate row
		validateChange row, 9
	}
	
	@Test
	void fiveDecreases() {
		def xml = PATTERN_START_TAG + '''
			  <pattern:directions>
			  		<cast-on>20</cast-on>
					<row>
						<decrease type="ssk">5</decrease>
						<knit>10</knit>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshal (xml)
		transform pattern
		Row row = pattern.directions.operations[1]
		validate row
		validateChange row, -5
	}
	
	@Test
	void inlineCompositeOperation() {
		def xml = PATTERN_START_TAG + '''
			  <pattern:directions>
			  		<cast-on>20</cast-on>
					<row>
						<knit>19</knit>
						<increase-into-next-stitch>
							<knit/>
							<purl/>
							<knit/>
							<purl/>
							<knit/>
						</increase-into-next-stitch>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshal (xml)
		transform pattern
		Row row = pattern.directions.operations[1]
		validate row
		validateChange row, 4
	}
	
	@Test
	void inlineInstructionRef() {
		def xml = PATTERN_START_TAG + '''
			  <pattern:directives>
			  	<pattern:instruction-definitions>
			  		<inline-instruction id="thing">
						<knit>15</knit>
					</inline-instruction>
			  	</pattern:instruction-definitions>
			  </pattern:directives>
			  <pattern:directions>
			  		<cast-on>20</cast-on>
					<row>
						<inline-instruction-ref ref="thing"/>
						<purl>5</purl>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshal (xml)
		transform pattern
		Row row = pattern.directions.operations[1]
		validate row
		validateChange row, 0
	}
	
	@Test
	void repeat() {
		def xml = PATTERN_START_TAG + '''
			  <pattern:directions>
			  		<cast-on>20</cast-on>
					<row>
						<repeat until="times" value="5">
							<knit>2</knit>
							<purl>2</purl>
						</repeat>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshal (xml)
		transform pattern
		Row row = pattern.directions.operations[1]
		validate row
		validateChange row, 0
	}
	
	@Test
	void castOn() {
		def xml = PATTERN_START_TAG + '''
			  <pattern:directions>
					<cast-on>20</cast-on>
			  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = unmarshal (xml)
		transform pattern
		CastOn castOn = pattern.directions.operations[0]
		validate castOn
	}
	
	private Pattern transform (Pattern pattern) {
		def params = new Parameters()
		params.pattern = pattern
		return program.validate(params)
	}
	
}
