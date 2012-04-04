package com.knitml.transform.gauge

import static com.knitml.core.units.Units.STITCHES_PER_INCH
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.text.StringContains.containsString
import static org.junit.Assert.assertThat

import org.junit.Test

import com.knitml.core.model.operations.block.ArrangeStitchesOnNeedles
import com.knitml.core.model.operations.block.CastOn
import com.knitml.core.model.pattern.Pattern
class StitchesOnNeedleTests extends AbstractTransformerTests {
	
	@Test
	void stitchesOnNeedles() {
		def xml = PATTERN_START_TAG + '''
  <pattern:general-information xml:lang="en">
    <pattern:name>Pattern</pattern:name>
    <pattern:description>Description</pattern:description>
  </pattern:general-information>
  <pattern:supplies>
    <pattern:yarn-types/>
    <pattern:needle-types>
      <pattern:needle-type id="_6d61gX3NEeGOzaJ8Z3SDGQ" type="circular">
        <pattern:size unit="US">00</pattern:size>
        <pattern:needles>
          <common:needle id="NeedleOne" label="Needle 1"/>
          <common:needle id="NeedleTwo" label="Needle 2"/>
        </pattern:needles>
      </pattern:needle-type>
    </pattern:needle-types>
    <pattern:accessories/>
  </pattern:supplies>
  <pattern:directions>
    <use-needles silent="true">
      <needle ref="NeedleOne"/>
    </use-needles>
    <cast-on style="long-tail">72</cast-on>
    <use-needles silent="true">
      <needle ref="NeedleOne"/>
      <needle ref="NeedleTwo"/>
    </use-needles>
    <arrange-stitches-on-needles>
      <needle ref="NeedleOne">36</needle>
      <needle ref="NeedleTwo">36</needle>
    </arrange-stitches-on-needles>
    <join-in-round/>
  </pattern:directions>
</pattern:pattern>'''
		Pattern pattern = transform (xml, '7 st/in', '6.5 st/in')
		def castOn = (CastOn)pattern.directions.operations[1]
		assertThat castOn.numberOfStitches, is (67)
		assertThat castOn.annotation, containsString ('5 fewer st')
		def arrange = (ArrangeStitchesOnNeedles)pattern.directions.operations[3]
		assertThat arrange.needles[0].numberOfStitches, is (33)
		assertThat arrange.needles[1].numberOfStitches, is (34)

		pattern = transform (xml, '7 st/in', '8 st/in')
		castOn = (CastOn)pattern.directions.operations[1]
		assertThat castOn.numberOfStitches, is (82)
		assertThat castOn.annotation, containsString ('10 more st')
		arrange = (ArrangeStitchesOnNeedles)pattern.directions.operations[3]
		assertThat arrange.needles[0].numberOfStitches, is (41)
		assertThat arrange.needles[1].numberOfStitches, is (41)

	}
	
}