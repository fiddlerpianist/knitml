/**
 * 
 */
package com.knitml.transform.gauge

import static com.knitml.transform.gauge.Unmarshaller.marshal

import org.custommonkey.xmlunit.XMLAssert

/**
 * @author Jonathan Whitall
 *
 */
public class XmlComparator {
	
	static void marshalAndCompare(Object object, String xml) {
		def marshalledXml = marshal(object)
		XMLAssert.assertXMLEqual(xml, marshalledXml)
	}
	
}
