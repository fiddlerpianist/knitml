package com.knitml.transform.gauge

import static com.knitml.core.units.Units.STITCHES_PER_INCH
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.text.StringContains.containsString
import static org.junit.Assert.assertThat

import javax.measure.Measure

import org.junit.Test

import com.knitml.core.model.operations.block.CastOn
import com.knitml.core.model.operations.block.Row
import com.knitml.core.model.pattern.Pattern
import com.knitml.core.units.Units
class RowHandlingTests extends AbstractTransformerTests {
	
	@Test
	void basicEvenRowPattern() {
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
		Pattern pattern = transform (xml, '5 st/in', '4 st/in')
		def castOn = (CastOn)pattern.directions.operations[0]
		assertThat castOn.numberOfStitches, is (16)
		assertThat castOn.annotation, containsString ('4 fewer st')
		def row = (Row)pattern.directions.operations[1]
		assertThat row.information.details[0].label, is ('with 16 st instead of 20')
		
		pattern = transform (xml, '5 st/in', '6 st/in')
		castOn = (CastOn)pattern.directions.operations[0]
		assertThat castOn.numberOfStitches, is (24)
		assertThat castOn.annotation, containsString ('4 more st')
		row = (Row)pattern.directions.operations[1]
		assertThat row.information.details[0].label, is ('with 24 st instead of 20')

		pattern = transform (xml, '5 st/in', '5 st/in')
		castOn = (CastOn)pattern.directions.operations[0]
		assertThat castOn.numberOfStitches, is (20)
		assertThat castOn.annotation, is (null)
		row = (Row)pattern.directions.operations[1]
		assertThat row.information, is (null)
	}
	
	@Test
	void unevenIncreaseAndDecreasePattern() {
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
		Pattern pattern = transform (xml, '5 st/in', '4 st/in')
		def row = (Row)pattern.directions.operations[1]
		assertThat row.information.details.size(), is (2)
		assertThat row.information.details[1].label, is ('inc. 7 st instead of 9')
		
		pattern = transform (xml, '5 st/in', '6 st/in')
		row = (Row)pattern.directions.operations[1]
		assertThat row.information.details.size(), is (2)
		assertThat row.information.details[1].label, is ('inc. 11 st instead of 9')

		pattern = transform (xml, '5 st/in', '5 st/in')
		assertThat pattern.directions.operations[1].information, is (null)
	}
	
	@Test
	void bigGaugeDifference() {
		def xml = PATTERN_START_TAG + '''
			  <pattern:directions>
					<cast-on>20</cast-on>
					<row>
						<knit>18</knit>
						<decrease type="ssk"/>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = transform (xml, '5 st/in', '2 st/in')
		def row = (Row)pattern.directions.operations[1]
		assertThat row.information.details.size(), is (2)
		assertThat row.information.details[0].label, is ('with 8 st instead of 20')
		assertThat row.information.details[1].label, is ('no st change instead of 1 st change')
	}

	@Test
	void fiveDecreasesPattern() {
		def xml = PATTERN_START_TAG + '''
			  <pattern:directions>
					<cast-on>20</cast-on>
					<row>
						<decrease type="ssk">5</decrease>
						<knit>10</knit>
					</row>
				  </pattern:directions>
			</pattern:pattern>'''
		Pattern pattern = transform (xml, '5 st/in', '4 st/in')
		def row = (Row)pattern.directions.operations[1]
		assertThat row.information.details.size(), is (2)
		assertThat row.information.details[1].label, is ('dec. 4 st instead of 5')

		pattern = transform (xml, '5 st/in', '6 st/in')
		row = (Row)pattern.directions.operations[1]
		assertThat row.information.details.size(), is (2)
		assertThat row.information.details[1].label, is ('dec. 6 st instead of 5')

		pattern = transform (xml, '5 st/in', '5 st/in')
		assertThat pattern.directions.operations[1].information, is (null)
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
		Pattern pattern = transform (xml, '5 st/in', '4 st/in')
		def row = (Row)pattern.directions.operations[1]
		assertThat row.information.details.size(), is (2)
		assertThat row.information.details[1].label, is ('inc. 3 st instead of 4')
		
		pattern = transform (xml, '5 st/in', '6 st/in')
		row = (Row)pattern.directions.operations[1]
		assertThat row.information.details.size(), is (2)
		assertThat row.information.details[1].label, is ('inc. 5 st instead of 4')

		pattern = transform (xml, '5 st/in', '5 st/in')
		assertThat pattern.directions.operations[1].information, is (null)
	}
	
}