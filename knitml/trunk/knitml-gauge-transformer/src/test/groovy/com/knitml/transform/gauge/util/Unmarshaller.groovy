/** 
 *  Copyright 2008, 2009 Jonathan Whitall
 * 
 *  This file is part of Gauge Transformer.
 * 
 *  Gauge Transformer is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Gauge Transformer is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Gauge Transformer.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.knitml.transform.gauge.util

import org.jibx.runtime.BindingDirectory
import org.jibx.runtime.IBindingFactory
import org.jibx.runtime.IMarshallingContext
import org.jibx.runtime.IUnmarshallingContext

import com.knitml.core.model.pattern.Pattern

/**
 * @author Jonathan Whitall
 *
 */
public class Unmarshaller {
	
	static Object unmarshal (Reader xml, Class rootClass) {
		IBindingFactory factory = getBindingFactory(rootClass)
		IUnmarshallingContext uctx = factory.createUnmarshallingContext()
		return uctx.unmarshalDocument(xml)
	}
	
	static Object unmarshal (String xml, Class rootClass) {
		return unmarshal (new StringReader(xml), rootClass)
	}
	
	static Object unmarshal (String xml) {
		return unmarshal (xml, Pattern)
	}
	
	static Object unmarshal (Reader xml) {
		return unmarshal (xml, Pattern)
	}
	
	static String marshal(Object object) {
		IMarshallingContext mctx = getBindingFactory(object.getClass()).createMarshallingContext()
		StringWriter writer = new StringWriter()
		mctx.indent = 4
		mctx.output = writer
		mctx.marshalDocument(object)
		return writer.toString()
	}
	
	private static IBindingFactory getBindingFactory(Class rootClass) {
		return BindingDirectory.getFactory(rootClass)
	}
	
}
