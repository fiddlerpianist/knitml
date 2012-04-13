/**
 * 
 */
package com.knitml.core.model.library

import static org.hamcrest.CoreMatchers.instanceOf
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.assertThat
import static test.support.JiBXTestUtils.marshalXmlAndCompare
import static test.support.JiBXTestUtils.unmarshalXml

import org.custommonkey.xmlunit.XMLUnit
import org.junit.BeforeClass
import org.junit.Test

import com.knitml.core.model.operations.block.Row
import com.knitml.core.model.operations.inline.Knit

class LibraryTests {
	@BeforeClass
	static void setUp() {
		XMLUnit.ignoreWhitespace = true
	}

	@Test
	void attributesAndInformationAndDirectives() {
		def xml = '''
<library:library xmlns:library="http://www.knitml.com/schema/library"
	xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xml:lang="en" xsi:schemaLocation="Schema location information here"
	version="0.7"
    namespace="http://example.knitml.com/test-namespace">
	<library:information>
		<library:name>Test Library Name</library:name>
		<library:description>The description</library:description>
	</library:information>
	<library:directives/>
	<library:definitions/>
</library:library>
		'''
		Library library = unmarshalXml(xml, Library)
		library.with {
			assertThat version, is ('0.7')
			assertThat languageCode, is ('en')
			assertThat schemaLocation, not (null)
			assertThat namespace, is ('http://example.knitml.com/test-namespace')
			assertThat information.name, is ('Test Library Name')
			assertThat information.description, is ('The description')
			assertThat namespace, not (null)
			assertThat instructionDefinitions.size(), is (0)
		}
	}

	@Test
	void yarns() {
		def xml = '''
<library:library xmlns:library="http://www.knitml.com/schema/library"
	xmlns="http://www.knitml.com/schema/operations" xmlns:common="http://www.knitml.com/schema/common"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xml:lang="en" xsi:schemaLocation="Schema location information here"
	version="0.7">
	<library:yarns>
		<common:yarn id="dark">
			<common:color rgb-value="F5EFDF" />
		</common:yarn>
		<common:yarn id="light" />
	</library:yarns>
	<library:definitions/>
</library:library>
		'''
		Library library = unmarshalXml(xml, Library)
		assertThat library.yarns, not (null)
		library.yarns.with {
			assertThat it.size(), is (2)
			assertThat it[0].id, is ('dark')
			assertThat it[0].color.rgbValue, is ('F5EFDF')
			assertThat it[1].id, is ('light')
		}
	}
	
	@Test
	void definitions() {
		def xml = '''
<library:library xmlns:library="http://www.knitml.com/schema/library"
	xmlns="http://www.knitml.com/schema/operations">
	<library:definitions>
		<instruction id="inst1">
			<row><knit/></row>
		</instruction>
	</library:definitions>
</library:library>
		'''
		Library library = unmarshalXml(xml, Library)
		assertThat library.instructionDefinitions, not (null)
		assertThat library.instructionDefinitions.size(), is (1)
		library.instructionDefinitions[0].with {
			assertThat id, is ('inst1')
			operations[0].with {
				assertThat it, instanceOf (Row)
				assertThat it.operations[0], instanceOf (Knit)
			}
		}
	}
}
