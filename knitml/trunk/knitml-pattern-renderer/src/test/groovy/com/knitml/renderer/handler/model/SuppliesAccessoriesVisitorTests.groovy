package com.knitml.renderer.handler.model

import static org.junit.Assert.assertThat
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.text.StringStartsWith.startsWith

import org.apache.commons.lang.StringUtils

import org.junit.Test
import org.junit.Before
import org.junit.Ignore
import org.junit.runner.JUnitCore

import com.knitml.core.model.pattern.Supplies;

import test.support.AbstractRenderingContextTests

class SuppliesAccessoriesVisitorTests extends AbstractRenderingContextTests {

	private static final LINE_BREAK = System.getProperty("line.separator");
	private static final INDENT = "    ";
	private static final ACCESSORIES = "Accessories:"
	private static final ACCESSORIES_HEADER = ACCESSORIES + LINE_BREAK + INDENT;
	
	@Test
	void noAccessories() {
		processXml '''<supplies xmlns="http://www.knitml.com/schema/pattern"><yarn-types/><needle-types/><accessories/></supplies>''', Supplies
		assertThat output, is (StringUtils.EMPTY)
	}
	
	@Test
	void oneStitchHolder() {
		processXml '''
        <supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common">
		  <yarn-types/>
          <needle-types/>
          <accessories>
            <common:stitch-holder id="sh1"/>
          </accessories>
        </supplies>''', Supplies
		assertThat output, startsWith (ACCESSORIES_HEADER + "1 stitch holder")
	}
	
	@Test
	void twoStitchHolders() {
		processXml '''
        <supplies xmlns="http://www.knitml.com/schema/pattern" xmlns:common="http://www.knitml.com/schema/common">
		  <yarn-types/>
          <needle-types/>
          <accessories>
            <common:stitch-holder id="sh1"/>
            <common:stitch-holder id="sh2"/>
          </accessories>
        </supplies>''', Supplies
		assertThat output, startsWith (ACCESSORIES_HEADER + "2 stitch holders")
	}
	
}
