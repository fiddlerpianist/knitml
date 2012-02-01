/**
 * 
 */
package com.knitml.core.model.header

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static test.support.JiBXTestUtils.marshalXmlAndCompare
import static test.support.JiBXTestUtils.unmarshalXml

import javax.measure.Measure

import org.custommonkey.xmlunit.XMLUnit
import org.junit.BeforeClass
import org.junit.Test

import com.knitml.core.common.NeedleStyle
import com.knitml.core.model.Pattern
import com.knitml.core.units.Units

class SuppliesTests {
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
	}
	
	@Test
	void yarns() {
		def xml = '''
		<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			<pattern:supplies>
				<pattern:yarn-types>
					<pattern:yarn-type id="lornas-1" brand="Lorna's Laces" category="Shepherd Sock" subcategory="Plus 2" weight="fingering" catalog-id="33032">
						<pattern:ball-length unit="yd">150</pattern:ball-length>
						<pattern:ball-weight unit="g">50</pattern:ball-weight>
						<pattern:thickness unit="wrap/in">16</pattern:thickness>
						<pattern:yarns>
							<common:yarn id="main-color" message-key="yarn.main-color" symbol="MC">
								<common:total-length unit="m">300</common:total-length>
								<common:total-weight unit="g">100</common:total-weight>
								<common:color name="watercolor" description="" number="233"/>
							</common:yarn>
							<common:yarn id="contrasting-color"/>
						</pattern:yarns>
					</pattern:yarn-type>
					<pattern:yarn-type id="lornas-2">
						<pattern:yarns>
							<common:yarn id="wacky-yarn"/>
						</pattern:yarns>
					</pattern:yarn-type>
				</pattern:yarn-types>
				<pattern:needle-types/>
				<pattern:accessories/>
			</pattern:supplies>
		</pattern:pattern>
		'''
		Pattern pattern = unmarshalXml(xml)
		def yarnTypes = pattern.supplies.yarnTypes
		assertThat yarnTypes.size(), is (2)
		yarnTypes[0].with {
			assertThat id, is ('lornas-1')
			assertThat brand, is ("Lorna's Laces")
			assertThat category, is ('Shepherd Sock')
			assertThat subcategory, is ('Plus 2')
			assertThat weight, is ('fingering')
			assertThat catalogId, is ('33032')
			ballLength.with {
				assertThat (it instanceof Measure, is (true))
				assertThat unit, is (Units.YARD)
				assertThat Integer.valueOf(value), is (150)
			}
			ballWeight.with {
				assertThat (it instanceof Measure, is (true))
				assertThat unit, is (Units.GRAM)
				assertThat Integer.valueOf(value), is (50)
			}
			thickness.with {
				assertThat (it instanceof Measure, is (true))
				assertThat unit, is (Units.WRAPS_PER_INCH)
				assertThat Integer.valueOf(value), is (16)
			}
		}
		yarnTypes[1].with {
			assertThat id, is ('lornas-2')
			assertThat brand, is (null)
			assertThat category, is (null)
			assertThat subcategory, is (null)
			assertThat weight, is (null)
			assertThat catalogId, is (null)
			assertThat ballLength, is (null)
			assertThat ballWeight, is (null)
			assertThat thickness, is (null)
		}
		def yarns = pattern.supplies.yarns
		assertThat yarns.size(), is (3)
		yarns[0].with {
			assertThat id, is ('main-color')
			assertThat yarnType, is (yarnTypes[0])
			assertThat messageKey, is ('yarn.main-color')
			assertThat symbol, is ('MC')
			totalLength.with {
				assertThat (it instanceof Measure, is (true))
				assertThat unit, is (Units.METER)
				assertThat Integer.valueOf(value), is (300)
			}
			totalWeight.with {
				assertThat (it instanceof Measure, is (true))
				assertThat unit, is (Units.GRAM)
				assertThat Integer.valueOf(value), is (100)
			}
			color.with {
				assertThat name, is ('watercolor')
				assertThat description, is ('')
				assertThat number, is ('233')
			}
		}
		yarns[1].with {
			assertThat id, is ('contrasting-color')
			assertThat yarnType, is (yarnTypes[0])
			assertThat messageKey, is (null)
			assertThat symbol, is (null)
			assertThat totalLength, is (null)
			assertThat totalWeight, is (null)
			assertThat color, is (null)
		}
		yarns[2].with {
			assertThat id, is ('wacky-yarn')
			assertThat yarnType, is (yarnTypes[1])
		}
		// assert that yarns are mapped to yarnTypes correctly
		assertThat yarnTypes[0].yarns[0], is (yarns[0]) 
		assertThat yarnTypes[0].yarns[1], is (yarns[1]) 
		assertThat yarnTypes[1].yarns[0], is (yarns[2]) 
		
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void needles() {
		def xml = '''
		<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			<pattern:supplies>
				<pattern:yarn-types/>
				<pattern:needle-types>
					<pattern:needle-type id="size1circ" type="circular" brand="Addi Turbo">
						<pattern:needles>
								<common:needle id="needle1" message-key="needle.needle1"/>
								<common:needle id="needle2" message-key="needle.needle2"/>
						</pattern:needles>
						<pattern:length unit="in">24</pattern:length>
						<pattern:size unit="US">000</pattern:size>
					</pattern:needle-type>
					<pattern:needle-type id="size2circ" type="straight">
						<pattern:needles>
							<common:needle id="needle3"/>
						</pattern:needles>
					</pattern:needle-type>
				</pattern:needle-types>
				<pattern:accessories/>
			</pattern:supplies>
		</pattern:pattern>
		'''
		Pattern pattern = unmarshalXml(xml)
		def needleTypes = pattern.supplies.needleTypes
		assertThat needleTypes.size(), is (2)
		needleTypes[0].with {
			assertThat id, is ('size1circ')
			assertThat style, is (NeedleStyle.CIRCULAR)
			assertThat brand, is ('Addi Turbo')
			length.with {
				assertThat (it instanceof Measure, is (true))
				assertThat unit, is (Units.INCH)
				assertThat Integer.valueOf(value), is (24)
			}
			needleSize.with {
				assertThat (it instanceof Measure, is (true))
				assertThat unit, is (Units.NEEDLE_SIZE_US)
				assertThat value, is ('000')
			}
		}
		needleTypes[1].with {
			assertThat id, is ('size2circ')
			assertThat style, is (NeedleStyle.STRAIGHT)
			assertThat brand, is (null)
			assertThat length, is (null)
			assertThat needleSize, is (null)
		}
		def needles = pattern.supplies.needles
		assertThat needles.size(), is (3)
		needles[0].with {
			assertThat id, is ('needle1')
			assertThat type, is (needleTypes[0])
			assertThat messageKey, is ('needle.needle1')
		}
		needles[1].with {
			assertThat id, is ('needle2')
			assertThat type, is (needleTypes[0])
			assertThat messageKey, is ('needle.needle2')
		}
		needles[2].with {
			assertThat id, is ('needle3')
			assertThat type, is (needleTypes[1])
			assertThat messageKey, is (null)
		}
		// assert that needles are mapped to needleTypes correctly
		assertThat needleTypes[0].needles[0], is (needles[0]) 
		assertThat needleTypes[0].needles[1], is (needles[1]) 
		assertThat needleTypes[1].needles[0], is (needles[2]) 
		marshalXmlAndCompare(pattern,xml)
	}
	
	@Test
	void stitchHolders() {
		def xml = '''
			<pattern:pattern xmlns:pattern="http://www.knitml.com/schema/pattern" xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common">
			  <pattern:supplies>
			    <pattern:yarn-types/>
			    <pattern:needle-types/>
                <pattern:accessories>
              	  <common:stitch-holder id="stitch-holder-1" label="Stitch Holder 1" message-key="stitch-holder.stitch-holder-1"/>
              	  <common:stitch-holder id="stitch-holder-2"/>
                </pattern:accessories>
              </pattern:supplies>
			</pattern:pattern>'''
		Pattern pattern = unmarshalXml(xml)
		def stitchHolders = pattern.supplies.stitchHolders
		stitchHolders[0].with {
			assertThat id, is ('stitch-holder-1')
			assertThat messageKey, is ('stitch-holder.stitch-holder-1')
			assertThat label, is ('Stitch Holder 1')
		}
		stitchHolders[1].with {
			assertThat id, is ('stitch-holder-2')
			assertThat messageKey, is (null)
			assertThat label, is (null)
		}
		marshalXmlAndCompare(pattern,xml)
	}
	
}
