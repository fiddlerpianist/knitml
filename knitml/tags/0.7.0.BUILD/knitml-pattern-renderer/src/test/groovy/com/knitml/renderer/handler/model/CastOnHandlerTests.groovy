package com.knitml.renderer.handler.model

import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringEndsWith.endsWith
import static org.hamcrest.text.StringStartsWith.startsWith
import static org.hamcrest.text.StringContains.containsString
import static org.junit.Assert.assertThat

import org.junit.Test

import test.support.AbstractRenderingContextTests

import com.knitml.core.model.operations.block.CastOn
import com.knitml.core.model.operations.inline.InlineCastOn
import com.knitml.core.model.pattern.Directions

class CastOnHandlerTests extends AbstractRenderingContextTests {


	@Test
	void castOnIdentifiedKey() {
		processXml '<cast-on style="long-tail" xmlns="http://www.knitml.com/schema/operations">5</cast-on>', CastOn
		assertThat output, startsWith ('Using the long-tail method, cast on 5')
	}

	@Test
	void castOnUnidentifiedKey() {
		processXml '<cast-on style="filbert" xmlns="http://www.knitml.com/schema/operations">5</cast-on>', CastOn
		assertThat output, startsWith ('Using the filbert method, cast on 5')
	}

	@Test
	void castOnWithAnnotation() {
		processXml '<cast-on annotation="3 st with modified gauge" xmlns="http://www.knitml.com/schema/operations">5</cast-on>', CastOn
		assertThat output.trim(), startsWith ('Cast on 5 stitches (3 st with modified gauge).')
	}

	@Test
	void usingNeedlesCastOn() {
		processXml PATTERN_START_TAG + '''
  <pattern:supplies>
    <pattern:yarn-types/>
    <pattern:needle-types>
      <pattern:needle-type id="needle-type-1">
        <pattern:needles>
          <common:needle id="NeedleOne" label="Needle 1"/>
        </pattern:needles>
      </pattern:needle-type>
    </pattern:needle-types>
    <pattern:accessories/>
  </pattern:supplies>
  <pattern:directions>
    <use-needles silent="true">
      <needle ref="NeedleOne"/>
    </use-needles>
    <cast-on>5</cast-on>
  </pattern:directions>
</pattern:pattern>
'''
		assertThat output.trim(), containsString ('Using Needle 1, cast on 5 stitches.')
	}

	@Test
	void usingNeedlesWithMethodCastOn() {
		processXml PATTERN_START_TAG + '''
  <pattern:supplies>
    <pattern:yarn-types/>
    <pattern:needle-types>
      <pattern:needle-type id="needle-type-1">
        <pattern:needles>
          <common:needle id="NeedleOne" label="Needle 1"/>
        </pattern:needles>
      </pattern:needle-type>
    </pattern:needle-types>
    <pattern:accessories/>
  </pattern:supplies>
  <pattern:directions>
    <use-needles silent="true">
      <needle ref="NeedleOne"/>
    </use-needles>
    <cast-on style="long-tail">5</cast-on>
  </pattern:directions>
</pattern:pattern>
'''
		assertThat output.trim(), containsString ('Cast 5 stitches onto Needle 1 using the long-tail method')
	}
	
	@Test
	void usingNeedlesWithAnnotationCastOn() {
		processXml PATTERN_START_TAG + '''
  <pattern:supplies>
    <pattern:yarn-types/>
    <pattern:needle-types>
      <pattern:needle-type id="needle-type-1">
        <pattern:needles>
          <common:needle id="NeedleOne" label="Needle 1"/>
        </pattern:needles>
      </pattern:needle-type>
    </pattern:needle-types>
    <pattern:accessories/>
  </pattern:supplies>
  <pattern:directions>
    <use-needles silent="true">
      <needle ref="NeedleOne"/>
    </use-needles>
    <cast-on annotation="good luck with that">5</cast-on>
  </pattern:directions>
</pattern:pattern>
'''
		assertThat output.trim(), containsString ('Using Needle 1, cast on 5 stitches (good luck with that).')
	}
	
	@Test
	void inlineCastOn() {
		renderingContext.with {
			engine.castOn 1
			engine.startNewRow()
		}

		processXml '<inline-cast-on xmlns="http://www.knitml.com/schema/operations">5</inline-cast-on>', InlineCastOn
		assertThat output.trim(), is ('cast on 5 stitches')
	}
}
